package paste3.bakersbox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Recipe Class - creates new recipe objects. Each recipe object has a name, a list of
 * recipeIngredients, and various other properties.
 */
public class Recipe {
    private String _recipeName;
    private List<RecipeIngredient> recipeIngredients;
    private float prepTime;
    private float cookTime;
    private float numberServings;
    private String typeServing;
    private String method;
    private float cost;  // initialised via setCost function in the main constructor

    /**
     * Main Constructor - initialises all the properties of a new Recipe object.
     * @param recipeName the recipe's name
     * @param recipeIngredients List of recipeIngredients
     * @param prepTime the preparation time for the recipe
     * @param cookTime how long the recipe is cooked / baked - a measure of appliance and/or
     *                 electricity / fue use
     * @param numberServings the number of servings produced by the recipe
     * @param typeServing the type of serving (slice, bar, cake, serving, etc)
     * @param method the recipe's method / instructions
     */
    public Recipe(String recipeName, List<RecipeIngredient> recipeIngredients, float prepTime,
                  float cookTime, float numberServings, String typeServing, String method) {
        this._recipeName = recipeName;
        this.recipeIngredients = recipeIngredients;
        this.prepTime = prepTime;
        this.cookTime = cookTime;
        this.numberServings = numberServings;
        this.typeServing = typeServing;
        this.method = method;
        setCost();
    }

    /**
     * Blank Constructor - creates a new blank recipe object
     */
    public Recipe() {
        this._recipeName = "";
        this.recipeIngredients = null;
        this.prepTime = 0;
        this.cookTime = 0;
        this.numberServings = 0;
        this.typeServing = "";
        this.method = "";
    }

    /**
     * "From Database" Constructor - takes the data retrieved from the database and constructs a
     * new Recipe object
     */
    public Recipe(Map<String, Object> recipeMap) {
        this._recipeName = (String) recipeMap.get("recipeName");
        this.recipeIngredients = RecipeIngredient
                .getRecipeItemList((List<Map<String, Object>>) recipeMap.get("recipeItems"));
        this.prepTime = ((Number) Objects.requireNonNull(recipeMap.get("prepTime"))).floatValue();
        this.cookTime = ((Number) Objects.requireNonNull(recipeMap.get("cookTime"))).floatValue();
        this.cost = ((Number) Objects.requireNonNull(recipeMap.get("cost"))).floatValue();
        this.numberServings = ((Number) Objects.requireNonNull(recipeMap.get("numberServings")))
                .floatValue();
        this.typeServing = (String) recipeMap.get("typeServing");
        this.method = (String) recipeMap.get("method");
    }

    /**
     * Turns a Recipe object into a simplified map, so that the recipeIngredients list doesn't
     * contain any ingredient or unit objects, for saving to the Database.
     * @return the recipeMap
     */
    public Map<String, Object> toMap() {
        Map<String, Object> recipeMap = new HashMap<>();
        recipeMap.put("recipeName", _recipeName);
        // The recipeIngredient list is simplified
        recipeMap.put("recipeItems", getSimplifiedRecipeIngredientList());
        recipeMap.put("prepTime", prepTime);
        recipeMap.put("cookTime", cookTime);
        recipeMap.put("cost", cost);
        recipeMap.put("numberServings", numberServings);
        recipeMap.put("typeServing", typeServing);
        recipeMap.put("method", method);

        return recipeMap;
    }

    /**
     * Simplifies the recipeIngredient List so that it doesn't contain Ingredient and Unit objects.
     * There are custom toMap() functions for both Ingredient and Unit.
     */
    public List<Object> getSimplifiedRecipeIngredientList() {
        List<Object> myList = new ArrayList<>();
        for (RecipeIngredient recipeIngredient : this.getRecipeIngredients()) {
            myList.add(recipeIngredient.toMap());
        }
        return myList;
    }

    /**
     * GETTERS AND SETTERS FOR ALL THE RECIPE OBJECT PROPERTIES
     */

    public String getRecipeName() {
        return _recipeName;
    }

    public void setRecipeName(String recipeName) {
        this._recipeName = recipeName;
    }

    public List<RecipeIngredient> getRecipeIngredients() {
        return recipeIngredients;
    }

    public void setRecipeIngredients(List<RecipeIngredient> recipeIngredients) {
        this.recipeIngredients = recipeIngredients;
    }

    public float getPrepTime() {
        return prepTime;
    }

    public void setPrepTime(float prepTime) {
        this.prepTime = prepTime;
    }

    public float getCookTime() {
        return cookTime;
    }

    public void setCookTime(float cookTime) {
        this.cookTime = cookTime;
    }

    public float getCost() {
        return cost;
    }

    /**
     * Calculates the cost of the recipe by looping through the recipeItem list and adding the
     * price of each item, to get the total ingredient cost for the recipe
     */
    public void setCost() {
        float totalCost = 0;
        for (RecipeIngredient recipeIngredient : recipeIngredients) {
            totalCost += recipeIngredient.calcPrice();
        }
        this.cost = totalCost;
    }

    public float getNumberServings() {
        return numberServings;
    }

    public void setNumberServings(float numberServings) {
        this.numberServings = numberServings;
    }

    public String getTypeServing() {
        return typeServing;
    }

    public void setTypeServing(String typeServing) {
        this.typeServing = typeServing;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

}
