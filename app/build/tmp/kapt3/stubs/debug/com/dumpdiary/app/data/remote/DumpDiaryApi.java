package com.dumpdiary.app.data.remote;

import com.dumpdiary.app.data.model.AuthRequestDto;
import com.dumpdiary.app.data.model.AppVersionDto;
import com.dumpdiary.app.data.model.AvatarResponseDto;
import com.dumpdiary.app.data.model.BowelLogDto;
import com.dumpdiary.app.data.model.AddFriendRequestDto;
import com.dumpdiary.app.data.model.FriendProfileDto;
import com.dumpdiary.app.data.model.MessageDto;
import com.dumpdiary.app.data.model.MonthlySummaryDto;
import com.dumpdiary.app.data.model.RefreshRequestDto;
import com.dumpdiary.app.data.model.RegisterRequestDto;
import com.dumpdiary.app.data.model.ResetPasswordRequestDto;
import com.dumpdiary.app.data.model.SendEmailCodeRequestDto;
import com.dumpdiary.app.data.model.SessionDto;
import com.dumpdiary.app.data.model.StreakSummaryDto;
import com.dumpdiary.app.data.model.UpdateProfileRequestDto;
import com.dumpdiary.app.data.model.UserProfileDto;
import com.dumpdiary.app.data.model.VerifyCodeDto;
import com.dumpdiary.app.data.model.VerifyEmailCodeRequestDto;
import com.dumpdiary.app.data.model.YearlyTrendPointDto;
import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u00ac\u0001\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\u00020\u00032\b\b\u0001\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u0018\u0010\u0007\u001a\u00020\b2\b\b\u0001\u0010\u0004\u001a\u00020\bH\u00a7@\u00a2\u0006\u0002\u0010\tJ\u0018\u0010\n\u001a\u00020\u00032\b\b\u0001\u0010\u000b\u001a\u00020\fH\u00a7@\u00a2\u0006\u0002\u0010\rJ\u0014\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00100\u000fH\u00a7@\u00a2\u0006\u0002\u0010\u0011J\u000e\u0010\u0012\u001a\u00020\u0013H\u00a7@\u00a2\u0006\u0002\u0010\u0011J\u0014\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\b0\u000fH\u00a7@\u00a2\u0006\u0002\u0010\u0011J\u0018\u0010\u0015\u001a\u00020\u00162\b\b\u0001\u0010\u0017\u001a\u00020\fH\u00a7@\u00a2\u0006\u0002\u0010\rJ\u000e\u0010\u0018\u001a\u00020\u0019H\u00a7@\u00a2\u0006\u0002\u0010\u0011J\u000e\u0010\u001a\u001a\u00020\u001bH\u00a7@\u00a2\u0006\u0002\u0010\u0011J\u001e\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\u001d0\u000f2\b\b\u0001\u0010\u001e\u001a\u00020\u001fH\u00a7@\u00a2\u0006\u0002\u0010 J\u0018\u0010!\u001a\u00020\"2\b\b\u0001\u0010\u0004\u001a\u00020#H\u00a7@\u00a2\u0006\u0002\u0010$J\u000e\u0010%\u001a\u00020\u0003H\u00a7@\u00a2\u0006\u0002\u0010\u0011J\u0018\u0010&\u001a\u00020\"2\b\b\u0001\u0010\u0004\u001a\u00020\'H\u00a7@\u00a2\u0006\u0002\u0010(J\u0018\u0010)\u001a\u00020\"2\b\b\u0001\u0010\u0004\u001a\u00020*H\u00a7@\u00a2\u0006\u0002\u0010+J\u0018\u0010,\u001a\u00020\u00032\b\b\u0001\u0010\u0004\u001a\u00020-H\u00a7@\u00a2\u0006\u0002\u0010.J\u0018\u0010/\u001a\u00020\u00032\b\b\u0001\u0010\u0004\u001a\u000200H\u00a7@\u00a2\u0006\u0002\u00101J\"\u00102\u001a\u00020\b2\b\b\u0001\u0010\u000b\u001a\u00020\f2\b\b\u0001\u0010\u0004\u001a\u00020\bH\u00a7@\u00a2\u0006\u0002\u00103J\u0018\u00104\u001a\u00020\u00192\b\b\u0001\u0010\u0004\u001a\u000205H\u00a7@\u00a2\u0006\u0002\u00106J\u0018\u00107\u001a\u0002082\b\b\u0001\u00109\u001a\u00020:H\u00a7@\u00a2\u0006\u0002\u0010;J\u0018\u0010<\u001a\u00020=2\b\b\u0001\u0010\u0004\u001a\u00020>H\u00a7@\u00a2\u0006\u0002\u0010?\u00a8\u0006@"}, d2 = {"Lcom/dumpdiary/app/data/remote/DumpDiaryApi;", "", "addFriend", "Lcom/dumpdiary/app/data/model/MessageDto;", "body", "Lcom/dumpdiary/app/data/model/AddFriendRequestDto;", "(Lcom/dumpdiary/app/data/model/AddFriendRequestDto;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "createLog", "Lcom/dumpdiary/app/data/model/BowelLogDto;", "(Lcom/dumpdiary/app/data/model/BowelLogDto;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteLog", "id", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getFriends", "", "Lcom/dumpdiary/app/data/model/FriendProfileDto;", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getLatestAppVersion", "Lcom/dumpdiary/app/data/model/AppVersionDto;", "getLogs", "getMonthlySummary", "Lcom/dumpdiary/app/data/model/MonthlySummaryDto;", "month", "getProfile", "Lcom/dumpdiary/app/data/model/UserProfileDto;", "getStreakSummary", "Lcom/dumpdiary/app/data/model/StreakSummaryDto;", "getYearlyTrend", "Lcom/dumpdiary/app/data/model/YearlyTrendPointDto;", "year", "", "(ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "login", "Lcom/dumpdiary/app/data/model/SessionDto;", "Lcom/dumpdiary/app/data/model/AuthRequestDto;", "(Lcom/dumpdiary/app/data/model/AuthRequestDto;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "logout", "refresh", "Lcom/dumpdiary/app/data/model/RefreshRequestDto;", "(Lcom/dumpdiary/app/data/model/RefreshRequestDto;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "register", "Lcom/dumpdiary/app/data/model/RegisterRequestDto;", "(Lcom/dumpdiary/app/data/model/RegisterRequestDto;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "resetPassword", "Lcom/dumpdiary/app/data/model/ResetPasswordRequestDto;", "(Lcom/dumpdiary/app/data/model/ResetPasswordRequestDto;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "sendEmailCode", "Lcom/dumpdiary/app/data/model/SendEmailCodeRequestDto;", "(Lcom/dumpdiary/app/data/model/SendEmailCodeRequestDto;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateLog", "(Ljava/lang/String;Lcom/dumpdiary/app/data/model/BowelLogDto;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateProfile", "Lcom/dumpdiary/app/data/model/UpdateProfileRequestDto;", "(Lcom/dumpdiary/app/data/model/UpdateProfileRequestDto;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "uploadAvatar", "Lcom/dumpdiary/app/data/model/AvatarResponseDto;", "avatar", "Lokhttp3/MultipartBody$Part;", "(Lokhttp3/MultipartBody$Part;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "verifyEmailCode", "Lcom/dumpdiary/app/data/model/VerifyCodeDto;", "Lcom/dumpdiary/app/data/model/VerifyEmailCodeRequestDto;", "(Lcom/dumpdiary/app/data/model/VerifyEmailCodeRequestDto;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public abstract interface DumpDiaryApi {
    
    @retrofit2.http.GET(value = "app/version")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getLatestAppVersion(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.dumpdiary.app.data.model.AppVersionDto> $completion);
    
    @retrofit2.http.POST(value = "auth/register")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object register(@retrofit2.http.Body()
    @org.jetbrains.annotations.NotNull()
    com.dumpdiary.app.data.model.RegisterRequestDto body, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.dumpdiary.app.data.model.SessionDto> $completion);
    
    @retrofit2.http.POST(value = "auth/login")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object login(@retrofit2.http.Body()
    @org.jetbrains.annotations.NotNull()
    com.dumpdiary.app.data.model.AuthRequestDto body, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.dumpdiary.app.data.model.SessionDto> $completion);
    
    @retrofit2.http.POST(value = "auth/send-email-code")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object sendEmailCode(@retrofit2.http.Body()
    @org.jetbrains.annotations.NotNull()
    com.dumpdiary.app.data.model.SendEmailCodeRequestDto body, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.dumpdiary.app.data.model.MessageDto> $completion);
    
    @retrofit2.http.POST(value = "auth/verify-email-code")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object verifyEmailCode(@retrofit2.http.Body()
    @org.jetbrains.annotations.NotNull()
    com.dumpdiary.app.data.model.VerifyEmailCodeRequestDto body, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.dumpdiary.app.data.model.VerifyCodeDto> $completion);
    
    @retrofit2.http.POST(value = "auth/reset-password")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object resetPassword(@retrofit2.http.Body()
    @org.jetbrains.annotations.NotNull()
    com.dumpdiary.app.data.model.ResetPasswordRequestDto body, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.dumpdiary.app.data.model.MessageDto> $completion);
    
    @retrofit2.http.POST(value = "auth/refresh")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object refresh(@retrofit2.http.Body()
    @org.jetbrains.annotations.NotNull()
    com.dumpdiary.app.data.model.RefreshRequestDto body, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.dumpdiary.app.data.model.SessionDto> $completion);
    
    @retrofit2.http.POST(value = "auth/logout")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object logout(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.dumpdiary.app.data.model.MessageDto> $completion);
    
    @retrofit2.http.GET(value = "me/profile")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getProfile(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.dumpdiary.app.data.model.UserProfileDto> $completion);
    
    @retrofit2.http.PUT(value = "me/profile")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateProfile(@retrofit2.http.Body()
    @org.jetbrains.annotations.NotNull()
    com.dumpdiary.app.data.model.UpdateProfileRequestDto body, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.dumpdiary.app.data.model.UserProfileDto> $completion);
    
    @retrofit2.http.Multipart()
    @retrofit2.http.POST(value = "me/avatar")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object uploadAvatar(@retrofit2.http.Part()
    @org.jetbrains.annotations.NotNull()
    okhttp3.MultipartBody.Part avatar, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.dumpdiary.app.data.model.AvatarResponseDto> $completion);
    
    @retrofit2.http.GET(value = "friends")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getFriends(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.dumpdiary.app.data.model.FriendProfileDto>> $completion);
    
    @retrofit2.http.POST(value = "friends")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object addFriend(@retrofit2.http.Body()
    @org.jetbrains.annotations.NotNull()
    com.dumpdiary.app.data.model.AddFriendRequestDto body, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.dumpdiary.app.data.model.MessageDto> $completion);
    
    @retrofit2.http.GET(value = "logs")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getLogs(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.dumpdiary.app.data.model.BowelLogDto>> $completion);
    
    @retrofit2.http.POST(value = "logs")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object createLog(@retrofit2.http.Body()
    @org.jetbrains.annotations.NotNull()
    com.dumpdiary.app.data.model.BowelLogDto body, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.dumpdiary.app.data.model.BowelLogDto> $completion);
    
    @retrofit2.http.PUT(value = "logs/{id}")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateLog(@retrofit2.http.Path(value = "id")
    @org.jetbrains.annotations.NotNull()
    java.lang.String id, @retrofit2.http.Body()
    @org.jetbrains.annotations.NotNull()
    com.dumpdiary.app.data.model.BowelLogDto body, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.dumpdiary.app.data.model.BowelLogDto> $completion);
    
    @retrofit2.http.DELETE(value = "logs/{id}")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteLog(@retrofit2.http.Path(value = "id")
    @org.jetbrains.annotations.NotNull()
    java.lang.String id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.dumpdiary.app.data.model.MessageDto> $completion);
    
    @retrofit2.http.GET(value = "stats/monthly")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getMonthlySummary(@retrofit2.http.Query(value = "month")
    @org.jetbrains.annotations.NotNull()
    java.lang.String month, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.dumpdiary.app.data.model.MonthlySummaryDto> $completion);
    
    @retrofit2.http.GET(value = "stats/streak")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getStreakSummary(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.dumpdiary.app.data.model.StreakSummaryDto> $completion);
    
    @retrofit2.http.GET(value = "stats/yearly")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getYearlyTrend(@retrofit2.http.Query(value = "year")
    int year, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.dumpdiary.app.data.model.YearlyTrendPointDto>> $completion);
}