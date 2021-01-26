package com.son.newsfeedfb.di
import com.son.newsfeedfb.MainActivity
import com.son.newsfeedfb.PostFragment
import com.son.newsfeedfb.RegisterUser
import com.son.newsfeedfb.SplashScreen
import com.son.newsfeedfb.ViewModel.AuthViewModel
import com.son.newsfeedfb.ViewModel.CommentViewModel
import com.son.newsfeedfb.ViewModel.GetListPostViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [FireBaseModule::class,RetrofitModule::class])
interface ClientComponent {
    fun inject(activity: MainActivity)
    fun inject(fragment: PostFragment)
    fun inject(activity: RegisterUser)
    fun inject(authViewModel: AuthViewModel)
    fun inject(getListPostViewModel: GetListPostViewModel)
    fun inject(commentViewModel: CommentViewModel)
    fun inject(splashScreen: SplashScreen)
}