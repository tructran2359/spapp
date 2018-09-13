package com.spgroup.spapp.di.component

import com.spgroup.spapp.di.ApplicationScoped
import com.spgroup.spapp.di.module.*
import com.spgroup.spapp.presentation.SPApplication
import com.spgroup.spapp.presentation.activity.*
import com.spgroup.spapp.presentation.fragment.*
import dagger.Component

@Component(modules = [
    AppModule::class,
    RepoModule::class,
    NetworkModule::class,
    MapperModule::class,
    GsonModule::class,
    UsecaseModule::class,
    ViewModelModule::class
])
@ApplicationScoped
interface AppComponent {

    fun inject(spApplication: SPApplication)

    fun inject(splashActivity: SplashActivity)

    fun inject(updateActivity: UpdateActivity)

    fun inject(homeActivity: HomeActivity)

    fun inject(pageActivity: PageActivity)

    fun inject(partnerListingActivity: PartnerListingActivity)

    fun inject(partnerDetailsActivity: PartnerDetailsActivity)

    fun inject(partnerInformationActivity: PartnerInformationActivity)

    fun inject(customiseNewActivity: CustomiseNewActivity)

    fun inject(onBoardingActivity: OnBoardingActivity)

    fun inject(pdfActivity: PdfActivity)

    fun inject(orderSummaryActivity: OrderSummaryActivity)

    fun inject(apiErrorActivity: ApiErrorActivity)

    fun inject(cartPartnerDetailFragment: CartPartnerDetailFragment)

    fun inject(categoryFragment: CategoryFragment)

    fun inject(detailInfoPartnerDetailFragment: DetailInfoPartnerDetailFragment)

    fun inject(onBoardingFragment: OnBoardingFragment)

    fun inject(weeklyMenuFragment: WeeklyMenuFragment)

}