package ru.skillbranch.gameofthrones.repositories

import android.net.Uri
import android.util.Log
import androidx.annotation.VisibleForTesting
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import ru.skillbranch.gameofthrones.App
import ru.skillbranch.gameofthrones.api.ApiFactory
import ru.skillbranch.gameofthrones.data.database.GoTDatabase
import ru.skillbranch.gameofthrones.data.local.entities.CharacterFull
import ru.skillbranch.gameofthrones.data.local.entities.CharacterItem
import ru.skillbranch.gameofthrones.data.remote.res.CharacterRes
import ru.skillbranch.gameofthrones.data.remote.res.HouseRes
import ru.skillbranch.gameofthrones.utils.extensions.toCharacter
import ru.skillbranch.gameofthrones.utils.extensions.toHouse
import ru.skillbranch.gameofthrones.utils.extensions.toShortName

object RootRepository {
    private val apiFactory = ApiFactory
    private val apiService = apiFactory.getApiService()
    private var houses = mutableListOf<HouseRes>()
    private var needHouses = mutableListOf<HouseRes>()
    private val needHousesWithCharacters = mutableListOf<Pair<HouseRes, List<CharacterRes>>>()
    private val database = GoTDatabase.getInstance(App.applicationContext())
    private val houseDao = database?.houseDao
    private val characterDao = database?.characterDao


    // copy all fun bodies and private funs from dmisuvorov github, cause need to go further
    // TODO - understand (need to learn RxJava) and maybe change

    /**
     * Получение данных о всех домах из сети
     * @param result - колбек содержащий в себе список данных о домах
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun getAllHouses(result: (houses: List<HouseRes>) -> Unit) {
        val disposable = getPageAndNext(1)
            .concatMap { response ->
                return@concatMap Observable.just(response.body() as List<HouseRes>)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    houses.addAll(it)
                    /*Log.d("M_RootRepository", it[0].toString())*/
                },
                { it.printStackTrace() },
                {
                    result(houses)
                    /*Log.d("M_RootRepository", "${houses.size}")*/
                })
    }

    private fun getPageAndNext(page: Int): Observable<Response<List<HouseRes>>> =
        apiService.getHousesByPage(page)
            .concatMap { response ->
                val headerLinkStr = response.headers().get("link")
                if (!headerLinkStr!!.contains(("rel=\"next\"")))
                    return@concatMap Observable.just(response)
                val nextLinkStr = headerLinkStr.substringBefore("rel=\"next\"")
                val headerLinkUrl = Uri.parse(nextLinkStr)
                val nextUrl = headerLinkUrl.getQueryParameter("page")
                return@concatMap Observable.just(response)
                    .concatWith(getPageAndNext(nextUrl!!.toInt()))
            }

    /**
     * Получение данных о требуемых домах по их полным именам из сети
     * @param houseNames - массив полных названий домов (смотри AppConfig)
     * @param result - колбек содержащий в себе список данных о домах
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun getNeedHouses(vararg houseNames: String, result: (houses: List<HouseRes>) -> Unit) {
        val disposable = getNeedHousesObservable(houseNames.toList())
            .subscribe(
                { if (it.isNullOrEmpty().not()) needHouses.add(it[0]) },
                { it.printStackTrace() },
                {
                    result(needHouses)
                    for (house in needHouses) Log.d("M_RootRepository", house.toString())
                })
    }

    private fun getNeedHousesObservable(houseNames: List<String>): Observable<List<HouseRes>> =
        Observable.fromArray(houseNames)
            .flatMapIterable { houses -> houses }
            .flatMap { house -> apiService.getHousesByName(house) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    /**
     * Получение данных о требуемых домах по их полным именам и персонажах в каждом из домов из сети
     * @param houseNames - массив полных названий домов (смотри AppConfig)
     * @param result - колбек содержащий в себе список данных о доме и персонажей в нем (Дом - Список Персонажей в нем)
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun getNeedHouseWithCharacters(
        vararg houseNames: String,
        result: (houses: List<Pair<HouseRes, List<CharacterRes>>>) -> Unit
    ) {
        val disposable = getNeedHousesObservable(houseNames.toList())
            .filter { housesRes -> housesRes.isNullOrEmpty().not() }
            .flatMap { houseRes -> getCharactersByHouse(houseRes[0]) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { needHousesWithCharacters.add(it) },
                { it.printStackTrace() },
                { result(needHousesWithCharacters) })
    }

    private fun getCharactersByHouse(houseRes: HouseRes): Observable<Pair<HouseRes, MutableList<CharacterRes>>>? =
        Observable.fromArray(houseRes.swornMembers)
            .flatMapIterable { swornMembersList -> swornMembersList }
            .flatMap { swornMember ->
                val idCharacter = swornMember.split("/").last()
                apiService.getCharacterById(idCharacter)
            }
            .map { characterRes -> characterRes.apply { houseId = houseRes.name.toShortName() } }
            .toList()
            .toObservable()
            .flatMap { characterRes -> Observable.just(houseRes to characterRes) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    /**
     * Запись данных о домах в DB
     * @param houses - Список персонажей (модель HouseRes - модель ответа из сети)
     * необходимо произвести трансформацию данных
     * @param complete - колбек о завершении вставки записей db
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun insertHouses(houses: List<HouseRes>, complete: () -> Unit) {
        val disposable = Completable.fromAction {
            houseDao?.insertHouses(houses.map { it.toHouse() })
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { complete() },
                { it.printStackTrace() })
    }

    /**
     * Запись данных о пересонажах в DB
     * @param Characters - Список персонажей (модель CharacterRes - модель ответа из сети)
     * необходимо произвести трансформацию данных
     * @param complete - колбек о завершении вставки записей db
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun insertCharacters(characters: List<CharacterRes>, complete: () -> Unit) {
        val disposable = Completable.fromAction {
            characterDao?.insertCharacters(characters.map { it.toCharacter() })
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { complete() },
                { it.printStackTrace() })
    }

    /**
     * При вызове данного метода необходимо выполнить удаление всех записей в db
     * @param complete - колбек о завершении очистки db
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun dropDb(complete: () -> Unit) {
        val disposable = Completable.fromAction {
            database?.clearAllTables()
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { complete() },
                { error -> error.printStackTrace() })
    }

    /**
     * Поиск всех персонажей по имени дома, должен вернуть список краткой информации о персонажах
     * дома - смотри модель CharacterItem
     * @param name - краткое имя дома (его первычный ключ)
     * @param result - колбек содержащий в себе список краткой информации о персонажах дома
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun findCharactersByHouseName(name: String, result: (characters: List<CharacterItem>) -> Unit) {
        // TODO delete mock
//        val starks = listOf(
//            CharacterItem("1", "Stark", "Stark 1", listOf("Starker 1"), listOf("")),
//            CharacterItem("2", "Stark", "Stark 2", listOf("Starker 2"), listOf("")),
//            CharacterItem("3", "Stark", "Stark 3", listOf("Starker 3"), listOf(""))
//            )
//        val lannisters = listOf(
//            CharacterItem("1", "Lannister", "Lannister 5", listOf("Lannisterer 5"), listOf("")),
//            CharacterItem("2", "Lannister", "Lannister 6", listOf("Lannisterer 6"), listOf("")),
//            CharacterItem("3", "Lannister", "Lannister 4", listOf("Lannisterer 4"), listOf("")),
//            CharacterItem("4", "Lannister", "Lannister 7", listOf("Lannisterer 7"), listOf(""))
//        )
//        when (name) {
//            "Stark" -> result(starks)
//            "Lannister" -> result(lannisters)
//            else -> result(listOf<CharacterItem>())
//        }
        val disposable = characterDao!!.findCharactersByHouseName(name)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result(it) },
                { it.printStackTrace() })
    }

    /**
     * Поиск персонажа по его идентификатору, должен вернуть полную информацию о персонаже
     * и его родственных отношения - смотри модель CharacterFull
     * @param id - идентификатор персонажа
     * @param result - колбек содержащий в себе полную информацию о персонаже
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun findCharacterFullById(id: String, result: (character: CharacterFull) -> Unit) {
        val disposable = characterDao!!.findCharacterFullById(id)
            .flatMap { characterFull ->
                Maybe.just(characterFull)
                    .flatMap {
                        characterFull.father ?: Maybe.just(characterFull)
                        if (characterFull.father!!.id.isNotEmpty())
                            characterDao.findRelativeCharacterById(characterFull.father.id)
                                .onErrorReturnItem(characterFull.father.copy(id = ""))
                                .toMaybe()
                                .flatMap { fatherRelative ->
                                    Maybe.just(characterFull.copy(father = fatherRelative))
                                }
                        else
                            Maybe.just(characterFull)
                    }


            }
            .flatMap { characterFull ->
                Maybe.just(characterFull)
                    .flatMap {
                        characterFull.mother ?: Maybe.just(characterFull)
                        if (characterFull.mother!!.id.isNotEmpty())
                            characterDao.findRelativeCharacterById(characterFull.mother.id)
                                .onErrorReturnItem(characterFull.mother.copy(id = ""))
                                .toMaybe()
                                .flatMap { motherRelative ->
                                    Maybe.just(characterFull.copy(mother = motherRelative))
                                }
                                .doOnComplete { Maybe.just(characterFull.copy(mother = null)) }
                        else Maybe.just(characterFull)
                    }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result(it) },
                { it.printStackTrace() },
                { throw IllegalArgumentException("No such character") })
    }

    /**
     * Метод возвращет true если в базе нет ни одной записи, иначе false
     * @param result - колбек о завершении очистки db
     */
    fun isNeedUpdate(result: (isNeed: Boolean) -> Unit) {
        val disposable = Single.zip(houseDao?.getCountEntity(),
            characterDao?.getCountEntity(),
            BiFunction { countHouses: Int, countCharacters: Int -> countHouses + countCharacters })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { countSum -> result(countSum == 0) },
                { it.printStackTrace() })
    }
}