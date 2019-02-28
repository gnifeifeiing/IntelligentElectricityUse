package com.zpxt.zhyd.retrofit;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.zpxt.zhyd.model.AlarmListModule;
import com.zpxt.zhyd.model.AlarmSearchListModule;
import com.zpxt.zhyd.model.BaseModule;
import com.zpxt.zhyd.model.HistoryAlarmListModule;
import com.zpxt.zhyd.model.HistoryListModule;
import com.zpxt.zhyd.model.MainFourthAlarmListMoudle;
import com.zpxt.zhyd.model.MovieModel;
import com.zpxt.zhyd.model.RealTimeDealAdviceModule;
import com.zpxt.zhyd.model.RealTimeListModule;
import com.zpxt.zhyd.model.RecentlyReportListModule;
import com.zpxt.zhyd.model.RecentlyReportOrganizationListModule;
import com.zpxt.zhyd.model.UserModule;
import com.zpxt.zhyd.model.VersionMessageModule;
import com.zpxt.zhyd.model.WeeklyDateListModuel;
import com.zpxt.zhyd.model.WeeklyDetailModule;

import org.json.JSONObject;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * Description:      API接口类
 * Autour：          LF
 * Date：            2017/7/5 16:31
 * <p>
 * retrofit注解：
 *
 * @HTTP：可以替代其他方法的任意一种
 * @HTTP(method = "get", path = "users/{user}", hasBody = false)
 * Call<ResponseBody> getFirstBlog(@Path("user") String user);
 * @Url：使用全路径复写baseUrl，适用于非统一baseUrl的场景。
 * @GET Call<ResponseBody> v3(@Url String url);
 * @Streaming:用于下载大文件
 * @Path：URL占位符，用于替换和动态更新,相应的参数必须使用相同的字符串被@Path进行注释
 * @GET("group/{id}/users") Call<ResponseBody> groupList(@Path("id") int groupId);
 * @Query,@QueryMap:查询参数，用于GET查询,需要注意的是@QueryMap可以约定是否需要encode@GET("group/users") Call<List<User>> groupList(@Query("id") int groupId);
 * Call<List<News>> getNews((@QueryMap(encoded=true) Map<String, String> options);
 * @Body:用于POST请求体，将实例对象根据转换方式转换为对应的json字符串参数，这个转化方式是GsonConverterFactory定义的。
 * @POST("add") Call<List<User>> addUser(@Body User user);
 * @Field，@FieldMap:Post方式传递简单的键值对,需要添加@FormUrlEncoded表示表单提交Content-Type:application/x-www-form-urlencoded
 * @FormUrlEncoded
 * @POST("user/edit") Call<User> updateUser(@Field("first_name") String first, @Field("last_name") String last);
 * @Part，@PartMap：用于POST文件上传其中@Part MultipartBody.Part代表文件，@Part("key") RequestBody代表参数需要添加@Multipart表示支持文件上传的表单，Content-Type: multipart/form-data
 */
public interface IApiSeivice {

    /********************示例模块**********************/
    @POST("login/auth")
    Call<JsonObject> getMovieList(@QueryMap Map<String, String> options);

    /******************************登录模块********************************/
    /**
     * 登录
     */
    @POST("login/auth")
    Call<UserModule> login(@QueryMap Map<String, String> options);

    //检查版本更新
    @GET("/api/v1/app/version")
    Call<VersionMessageModule> checkVersion();

    /**
     * 修改密码
     * @param options
     * @return
     */
    @POST("login/updatePassword")
    Call<BaseModule> updatePwd(@QueryMap Map<String, String> options);

    /**
     * 退出登录
     * @return
     */
    @POST("login/appSignOut")
    Call<BaseModule> signOut();


    /******************************实时数据模块********************************/
    /**
     * 实时数据列表
     */
    @GET("nodeList/queryNodeList")
    Call<AlarmListModule> getNodeList(@QueryMap Map<String, String> options);

    /**
     * 搜索节点列表
     */
    @GET("nodeList/search")
    Call<AlarmSearchListModule> getSearchList(@QueryMap Map<String, String> options);

    /**
     * 实时告警数据详情
     */
    @GET("realtimedata/queryInfoById")
    Call<RealTimeListModule> getRealTimeInfo(@QueryMap Map<String, String> options);

    /**
     * 处理并保存告警信息
     */
    @POST("historyAlarm/dealAlarm")
    Call<RealTimeDealAdviceModule> saveDealWithAdvice(@Body Map<String, String> options);


    /******************************最近告警模块********************************/
    /**
     * 最近告警数据列表获取
     */
    @POST("realtimedata/getTimelyAlarm")
    Call<RecentlyReportListModule> getRecentlyReportList(@Body Map<String, String> options);

    /**
     * 最近告警组织信息列表获取
     */
    @GET("realtimedata/getOrgName")
    Call<RecentlyReportOrganizationListModule> getRecentlyReportOrganizationList();


    /******************************历史告警模块********************************/
    /**
     * 历史报警列表查询
     */
    @POST("historyAlarm/queryAlarmList")
    Call<HistoryAlarmListModule> queryAlarmList(@Body Map<String, String> options);
    /**
     * 报警设备历史列表
     */
    @GET("historyAlarm/getAlarmData")
    Call<MainFourthAlarmListMoudle> queryAlarmEquipmentList(@QueryMap Map<String, String> options);


    /******************************历史数据模块********************************/
    /**
     * 历史报警列表查询
     */
    @POST("eleData")
    Call<HistoryListModule> queryEleData(@Body Map<String, String> options);


    /******************************检测周报模块********************************/
    /**
     * 历史报警列表查询
     */
    @POST("inspectionReport/queryData")
    Call<WeeklyDateListModuel> queryWeelyListDate(@Body Map<String, String> options);

    /**
     * 历史报警列表查询
     */
    @GET("inspectionReport/queryTable")
    Call<WeeklyDetailModule> queryWeelyData(@QueryMap Map<String, String> options);

    /******************************消息推送模块********************************/
    /**
     * 消息推送类型
     */
    @GET("realtimedata/setAlarmType")
    Call<BaseModule> savePushSetting(@QueryMap Map<String, String> options);
}
