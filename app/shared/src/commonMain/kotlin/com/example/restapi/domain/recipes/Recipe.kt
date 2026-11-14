package com.example.restapi.domain.recipes

internal data class Recipe(
    val id: Int,
    val name: String,
    val ingredients: List<String>,
    val instructions: List<String>,
    val prepTimeMinutes: Int,
    val cookTimeMinutes: Int,
    val servings: Int,
    val difficulty: String,
    val cuisine: String,
    val caloriesPerServing: Int,
    val mealTypes: List<String>,
    val image: String,
    val rating: Double,
    val reviewCount: Int,
) {
    val totalMinutes: Int get() = prepTimeMinutes + cookTimeMinutes
}

internal enum class RecipeSort { Default, Rating, Quickest, Lightest }

/** Everything the user can choose on the home screen. Empty sets mean "no restriction". */
internal data class RecipeFilter(
    val query: String = "",
    val mealTypes: Set<String> = emptySet(),
    val cuisines: Set<String> = emptySet(),
    val difficulties: Set<String> = emptySet(),
    val sort: RecipeSort = RecipeSort.Default,
) {
    /** Number of chosen options in the drop-downs (the sort order is not a filter). */
    val activeCount: Int get() = mealTypes.size + cuisines.size + difficulties.size

    val isActive: Boolean get() = activeCount > 0 || query.isNotBlank()
}

/**
 * Search, multi-select filters and sorting. The query is looked for in the name, the cuisine and the
 * ingredients. Inside one group the options are alternatives (Breakfast OR Lunch), the groups are combined with AND.
 */
internal fun List<Recipe>.applyFilter(filter: RecipeFilter): List<Recipe> {
    val text = filter.query.trim()
    val matched = filter { recipe ->
        (filter.mealTypes.isEmpty() || recipe.mealTypes.any { it in filter.mealTypes }) &&
            (filter.cuisines.isEmpty() || recipe.cuisine in filter.cuisines) &&
            (filter.difficulties.isEmpty() || recipe.difficulty in filter.difficulties) &&
            (text.isEmpty() ||
                recipe.name.contains(text, ignoreCase = true) ||
                recipe.cuisine.contains(text, ignoreCase = true) ||
                recipe.ingredients.any { it.contains(text, ignoreCase = true) })
    }
    return when (filter.sort) {
        RecipeSort.Default -> matched
        RecipeSort.Rating -> matched.sortedByDescending { it.rating }
        RecipeSort.Quickest -> matched.sortedBy { it.totalMinutes }
        RecipeSort.Lightest -> matched.sortedBy { it.caloriesPerServing }
    }
}
