package com.example.restapi.domain.recipes

import kotlin.test.Test
import kotlin.test.assertEquals

class FilterRecipesTest {

    private fun recipe(
        id: Int,
        name: String,
        cuisine: String,
        ingredients: List<String>,
        meals: List<String>,
        difficulty: String = "Easy",
        minutes: Int = 30,
        kcal: Int = 300,
        rating: Double = 4.5,
    ) = Recipe(
        id = id, name = name, ingredients = ingredients, instructions = emptyList(),
        prepTimeMinutes = minutes, cookTimeMinutes = 0, servings = 4, difficulty = difficulty, cuisine = cuisine,
        caloriesPerServing = kcal, mealTypes = meals, image = "", rating = rating, reviewCount = 10,
    )

    private val pizza = recipe(1, "Margherita Pizza", "Italian", listOf("Pizza dough", "Basil"), listOf("Dinner", "Lunch"), minutes = 40, kcal = 500, rating = 4.6)
    private val pancakes = recipe(2, "Pancakes", "American", listOf("Flour", "Milk"), listOf("Breakfast"), minutes = 15, kcal = 300, rating = 4.9)
    private val soup = recipe(3, "Tomato Soup", "Italian", listOf("Tomato"), listOf("Lunch"), difficulty = "Medium", minutes = 25, kcal = 150, rating = 4.2)
    private val all = listOf(pizza, pancakes, soup)

    @Test
    fun emptyFilterKeepsEverything() {
        assertEquals(all, all.applyFilter(RecipeFilter()))
        assertEquals(all, all.applyFilter(RecipeFilter(query = "   ")))
    }

    @Test
    fun queryLooksInNameCuisineAndIngredients() {
        assertEquals(listOf(pizza), all.applyFilter(RecipeFilter(query = "margherita")))
        assertEquals(listOf(pancakes), all.applyFilter(RecipeFilter(query = "AMERICAN")))
        assertEquals(listOf(pancakes), all.applyFilter(RecipeFilter(query = "milk")))
    }

    @Test
    fun severalMealTypesAreAlternatives() {
        val result = all.applyFilter(RecipeFilter(mealTypes = setOf("Breakfast", "Dinner")))
        assertEquals(listOf(pizza, pancakes), result)
    }

    @Test
    fun differentGroupsAreCombined() {
        val result = all.applyFilter(RecipeFilter(mealTypes = setOf("Lunch"), cuisines = setOf("Italian"), difficulties = setOf("Medium")))
        assertEquals(listOf(soup), result)
        assertEquals(emptyList(), all.applyFilter(RecipeFilter(query = "pizza", mealTypes = setOf("Breakfast"))))
    }

    @Test
    fun sorting() {
        assertEquals(listOf(pancakes, pizza, soup), all.applyFilter(RecipeFilter(sort = RecipeSort.Rating)))
        assertEquals(listOf(pancakes, soup, pizza), all.applyFilter(RecipeFilter(sort = RecipeSort.Quickest)))
        assertEquals(listOf(soup, pancakes, pizza), all.applyFilter(RecipeFilter(sort = RecipeSort.Lightest)))
    }

    @Test
    fun activeCountIgnoresSort() {
        assertEquals(0, RecipeFilter(sort = RecipeSort.Rating).activeCount)
        assertEquals(3, RecipeFilter(mealTypes = setOf("A", "B"), cuisines = setOf("C")).activeCount)
    }
}
