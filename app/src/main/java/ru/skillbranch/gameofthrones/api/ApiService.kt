package com.achesnovitskiy.empoyees.api

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query
import ru.skillbranch.gameofthrones.data.remote.res.HouseRes

interface ApiService {
    @GET("houses")
    fun getHouses(@Query("page") page: Int, @Query("pageSize") pageSize: Int = 50): Observable<Array<HouseRes>>
}