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
    @Multipart
    @POST("postBatch")
    Call<DefaultResponse> postCoffeeBatch(@Part("Age") RequestBody age,@Part("Trees") RequestBody trees,@Part("TotalTrees") RequestBody totalTrees,@Part("Branches") RequestBody branchesAmmount,@Part("Name") RequestBody name,@Part("Stems") RequestBody stems,@Part("DeviceUuid") RequestBody device);

    @Multipart
    @POST("postTree")
    Call<DefaultResponse> postCoffeeTree(@Part("BatchId") RequestBody batchID,@Part("IndexTree") RequestBody indexTree);

    @Multipart
    @POST("postCoordinate")
    Call<DefaultResponse> postCoordinate(@Part("BatchId") RequestBody batchID,@Part("IndexCoordinate") RequestBody indexCoordinate,@Part("Lat") RequestBody lat,@Part("Lng") RequestBody lng);

    @Multipart
    @POST("postBranch")
    Call<DefaultResponse> postCoffeeBranch(@Part("TreeId") RequestBody treeID,@Part("IndexBranch") RequestBody indexBranch,@Part("Date") RequestBody date,@Part("StemId") RequestBody stemId);

    @Multipart
    @POST("postFrame")
    Call<DefaultResponse> postCoffeeFrame(@Part("BranchId") RequestBody branchID, @Part("Factor") RequestBody factor, @Part("Time") RequestBody time, @Part("file\"; filename=\"image.jpg") RequestBody file);

    @Multipart
    @POST("postDevice")
    Call<DefaultResponse> createDevice(@Part("Uuid") RequestBody udid, @Part("Name") RequestBody device, @Part("Model") RequestBody model, @Part("Product") RequestBody product);

}
