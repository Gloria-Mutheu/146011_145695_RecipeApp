package com.example.a146011_145695.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.a146011_145695.activities.CategoryMealsActivity
import com.example.a146011_145695.activities.MealActivity
import com.example.a146011_145695.adapters.CategoriesAdapter
import com.example.a146011_145695.adapters.MostPopularAdapter
import com.example.a146011_145695.databinding.FragmentHomeBinding
import com.example.a146011_145695.pojo.Meal
import com.example.a146011_145695.pojo.MealsByCategory
import com.example.a146011_145695.videoModel.HomeViewModel


class HomeFragment : Fragment() {
    private lateinit var binding:FragmentHomeBinding
    private lateinit var homeMVVM:HomeViewModel
    private lateinit var randomMeal: Meal
    private lateinit var popularItemsAdapter:MostPopularAdapter
    private lateinit var categoriesAdapter: CategoriesAdapter


    companion object{

        const val MEAL_ID="com.example.a146011_145695.fragments.idMeal"
        const val MEAL_NAME="com.example.a146011_145695.fragments.nameMeal"
        const val MEAL_THUMB="com.example.a146011_145695.fragments.thumbMeal"
        const val CATEGORY_NAME="com.example.a146011_145695.fragments.categoryName"

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeMVVM = ViewModelProvider(this).get(HomeViewModel::class.java)
        popularItemsAdapter= MostPopularAdapter()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preparePopularItemsRecyclerView()
        homeMVVM.getRandomMeal()
        observerRandomMeal()
        onRandomMealClick()

        homeMVVM.getPopularItems()
        observePopularItemsLiveData()
        onPopularItemClick()

        prepareCategoriesRecyclerView()
        homeMVVM.getCategories()
        observeCategoriesLivedata()
        onCategoryClick()




    }

    private fun onCategoryClick() {
        categoriesAdapter.onItemClick={category ->
            val intent=Intent(activity,CategoryMealsActivity::class.java)
            intent.putExtra(CATEGORY_NAME,category.strCategory)
            startActivity(intent)


        }
    }

    private fun prepareCategoriesRecyclerView() {
        categoriesAdapter=CategoriesAdapter()
        binding.recViewCategories.apply{
            layoutManager=GridLayoutManager(context,3,GridLayoutManager.VERTICAL,false)
            adapter=categoriesAdapter
        }
    }

    private fun observeCategoriesLivedata() {
        homeMVVM.observeCategoriesLiveData().observe(viewLifecycleOwner, Observer {categories->
            categoriesAdapter.setCategoryList(categories)

        } )
    }

    private fun onPopularItemClick() {
        popularItemsAdapter.onItemClick={
            meal->
            val intent=Intent(activity,MealActivity::class.java)
            intent.putExtra(MEAL_ID,meal.idMeal)
            intent.putExtra(MEAL_NAME,meal.strMeal)
            intent.putExtra(MEAL_THUMB,meal.strMealThumb)
            startActivity(intent)
        }
    }

    private fun preparePopularItemsRecyclerView() {
        binding.recViewMealsPopular.apply{
            layoutManager=LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
            adapter=popularItemsAdapter
        }
    }

    private fun observePopularItemsLiveData() {
        homeMVVM.observePopularItemsLiveData().observe(viewLifecycleOwner)
             { mealList->
                 popularItemsAdapter.setMeals(mealsList = mealList as ArrayList<MealsByCategory>)



        }
    }

    private fun onRandomMealClick() {
        binding.randomMealCard.setOnClickListener {
            val intent= Intent(activity,MealActivity::class.java)
            intent.putExtra(MEAL_ID,randomMeal.idMeal)
            intent.putExtra(MEAL_NAME,randomMeal.strMeal)
            intent.putExtra(MEAL_THUMB,randomMeal.strMealThumb)
            startActivity(intent)
        }
    }


    private fun observerRandomMeal() {
        homeMVVM.observeRandomMealLivedata().observe(viewLifecycleOwner
        ) { meal ->
            Glide.with(this@HomeFragment)
                .load(meal!!.strMealThumb)
                .into(binding.imgRandomMeal)
            this.randomMeal = meal

        }
    }

}