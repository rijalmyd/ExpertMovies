package com.rijaldev.search.di

import android.content.Context
import com.rijaldev.expertmovies.di.SearchModuleDependencies
import com.rijaldev.search.SearchFragment
import dagger.BindsInstance
import dagger.Component

@Component(
    dependencies = [SearchModuleDependencies::class]
)
interface SearchComponent {
    fun inject(searchFragment: SearchFragment)

    @Component.Builder
    interface Builder {
        fun context(@BindsInstance context: Context): Builder
        fun appDependencies(searchModuleDependencies: SearchModuleDependencies): Builder
        fun build(): SearchComponent
    }
}