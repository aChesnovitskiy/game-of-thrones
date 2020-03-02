package ru.skillbranch.gameofthrones.repositories

import android.net.Uri
import android.util.Log
import androidx.annotation.VisibleForTesting
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import ru.skillbranch.gameofthrones.api.ApiFactory
import ru.skillbranch.gameofthrones.data.local.entities.CharacterFull
import ru.skillbranch.gameofthrones.data.local.entities.CharacterItem
import ru.skillbranch.gameofthrones.data.remote.res.CharacterRes
import ru.skillbranch.gameofthrones.data.remote.res.HouseRes
import ru.skillbranch.gameofthrones.utils.extensions.shortName

object RootRepository {
    private val apiFactory = ApiFactory
    private val apiService = apiFactory.getApiService()
    private var houses = mutableListOf<HouseRes>()
    private var needHouses = mutableListOf<HouseRes>()
    private val needHousesWithCharacters = mutableListOf<Pair<HouseRes, List<CharacterRes>>>()

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
            .map { characterRes -> characterRes.apply { houseId = houseRes.name.shortName() } }
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
        //TODO implement me
    }

    /**
     * Запись данных о пересонажах в DB
     * @param Characters - Список персонажей (модель CharacterRes - модель ответа из сети)
     * необходимо произвести трансформацию данных
     * @param complete - колбек о завершении вставки записей db
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun insertCharacters(Characters: List<CharacterRes>, complete: () -> Unit) {
        //TODO implement me
    }

    /**
     * При вызове данного метода необходимо выполнить удаление всех записей в db
     * @param complete - колбек о завершении очистки db
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun dropDb(complete: () -> Unit) {
        //TODO implement me
    }

    /**
     * Поиск всех персонажей по имени дома, должен вернуть список краткой информации о персонажах
     * дома - смотри модель CharacterItem
     * @param name - краткое имя дома (его первычный ключ)
     * @param result - колбек содержащий в себе список краткой информации о персонажах дома
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun findCharactersByHouseName(name: String, result: (characters: List<CharacterItem>) -> Unit) {
        //TODO implement me
    }

    /**
     * Поиск персонажа по его идентификатору, должен вернуть полную информацию о персонаже
     * и его родственных отношения - смотри модель CharacterFull
     * @param id - идентификатор персонажа
     * @param result - колбек содержащий в себе полную информацию о персонаже
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun findCharacterFullById(id: String, result: (character: CharacterFull) -> Unit) {
        //TODO implement me
    }

    /**
     * Метод возвращет true если в базе нет ни одной записи, иначе false
     * @param result - колбек о завершении очистки db
     */
    fun isNeedUpdate(result: (isNeed: Boolean) -> Unit) {
        //TODO implement me
    }

}