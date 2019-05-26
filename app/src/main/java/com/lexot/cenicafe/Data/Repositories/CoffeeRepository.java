package com.lexot.cenicafe.Data.Repositories;

import android.content.Context;
import android.os.Build;

import com.lexot.cenicafe.Data.ApiResponses.DefaultResponse;
import com.lexot.cenicafe.Data.ApiResponses.FrameResponse;
import com.lexot.cenicafe.Data.IRestClient;
import com.lexot.cenicafe.Data.RestClient;
import com.lexot.cenicafe.Models.CoffeeBatch;
import com.lexot.cenicafe.Models.CoffeeBranch;
import com.lexot.cenicafe.Models.CoffeeFrame;
import com.lexot.cenicafe.Models.CoffeeTree;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit.Call;

public class CoffeeRepository {
    private final IRestClient apiService;

    public CoffeeRepository(Context context) {
        apiService = new RestClient(context).getApiService();
    }

    public Call<DefaultResponse> PostCoffeeBatch(CoffeeBatch coffeeBatch, String udid) {
        return apiService.postCoffeeBatch(
                RequestBody.create(MediaType.parse("multipart/form-data"),coffeeBatch.Age.toString()),
                RequestBody.create(MediaType.parse("multipart/form-data"),coffeeBatch.Trees.toString()),
                RequestBody.create(MediaType.parse("multipart/form-data"),coffeeBatch.BranchesAmmount.toString()),
                RequestBody.create(MediaType.parse("multipart/form-data"),coffeeBatch.Name.toString()),
                RequestBody.create(MediaType.parse("multipart/form-data"),coffeeBatch.Stems.toString()),
                RequestBody.create(MediaType.parse("text/plain"),udid));
    }

    public Call<DefaultResponse> PostCoffeeTree(CoffeeTree coffeeTree, Integer parentBackedId) {
        return apiService.postCoffeeTree(
                RequestBody.create(MediaType.parse("multipart/form-data"),parentBackedId.toString()),
                RequestBody.create(MediaType.parse("multipart/form-data"),coffeeTree.Index.toString()));
    }

    public Call<DefaultResponse> PostCoffeeBranch(CoffeeBranch coffeeBranch, Integer parentBackedId) {
        RequestBody date =
                RequestBody.create(MediaType.parse("multipart/form-data"),new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        return apiService.postCoffeeBranch(
                RequestBody.create(MediaType.parse("multipart/form-data"),parentBackedId.toString()),
                RequestBody.create(MediaType.parse("multipart/form-data"),coffeeBranch.Type.toString()),
                RequestBody.create(MediaType.parse("multipart/form-data"),coffeeBranch.Index.toString()),
                date,
                RequestBody.create(MediaType.parse("multipart/form-data"),coffeeBranch.StemId.toString()));
    }

    public Call<DefaultResponse> PostCoffeeFrame(CoffeeFrame coffeeFrame, RequestBody file, Integer parentBackedId) {
        return apiService.postCoffeeFrame(
                RequestBody.create(MediaType.parse("text/plain"),parentBackedId.toString()),
                RequestBody.create(MediaType.parse("text/plain"),coffeeFrame.Factor.toString()),
                RequestBody.create(MediaType.parse("text/plain"),coffeeFrame.Time.toString()),
                file);
    }

    public Call<DefaultResponse> CreateDevice(String udid) {
        return apiService.createDevice(
                RequestBody.create(MediaType.parse("text/plain"),udid),
                RequestBody.create(MediaType.parse("text/plain"),android.os.Build.DEVICE),
                RequestBody.create(MediaType.parse("text/plain"), Build.MODEL),
                RequestBody.create(MediaType.parse("text/plain"), Build.PRODUCT));
    }

    public Call<List<CoffeeBranch>>GetBranches(String user){
        return apiService.getBranches(user);
    }
}