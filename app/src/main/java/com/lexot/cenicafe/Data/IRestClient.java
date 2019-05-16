package com.lexot.cenicafe.Data;

import com.lexot.cenicafe.Data.ApiResponses.BranchResponse;
import com.lexot.cenicafe.Data.ApiResponses.DefaultResponse;
import com.lexot.cenicafe.Data.ApiResponses.FrameResponse;
import com.lexot.cenicafe.Models.CoffeeBranch;
import com.lexot.cenicafe.Models.CoffeeFrame;
import com.squareup.okhttp.RequestBody;

import java.util.List;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.http.Query;

public interface IRestClient {
    /**
     * Obtener restaurantes
     *
     * @return Promesa de respuesta del servicio
     */
    @GET("getBranchs/{user}")
    Call<List<CoffeeBranch>> getBranches(@Path("user") String user);

    @GET("getFrames/{branchId}")
    Call<List<CoffeeFrame>> getFrames(@Path("branchId") String branchId);

    @Multipart
    @POST("postBatch")
    Call<DefaultResponse> postCoffeeBatch(@Part("Age") RequestBody age,@Part("Trees") RequestBody trees,@Part("Branches") RequestBody branchesAmmount,@Part("Name") RequestBody name,@Part("Stems") RequestBody stems,@Part("Device") RequestBody device);

    @Multipart
    @POST("postTree")
    Call<DefaultResponse> postCoffeeTree(@Part("BatchID") RequestBody batchID,@Part("Index") RequestBody index,@Part("Lat") RequestBody lat,@Part("Lng") RequestBody lng);

    @Multipart
    @POST("postBranch")
    Call<DefaultResponse> postCoffeeBranch(@Part("TreeID") RequestBody treeID,@Part("Type") RequestBody type,@Part("Index") RequestBody index,@Part("Date") RequestBody date,@Part("StemID") RequestBody stemID);

    @Multipart
    @POST("postFrame")
    Call<DefaultResponse> postCoffeeFrame(@Part("BranchID") RequestBody branchID, @Part("Factor") RequestBody factor, @Part("Time") RequestBody time, @Part("file\"; filename=\"image.jpg") RequestBody file);

    @Multipart
    @POST("postDevice")
    Call<DefaultResponse> createDevice(@Part("Udid") RequestBody udid, @Part("Device") RequestBody device, @Part("Model") RequestBody model, @Part("Product") RequestBody product);

}
