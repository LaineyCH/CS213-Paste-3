package paste3.bakersbox;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Recipe {
    private String _recipeName;
    List<RecipeIngredient> recipeItems;
    float prepTime;
    float cookTime;
    float numberServings;
    String typeServing;
    String method;
    float cost;  // set via setCost function in the mai constructor

    // Main Constructor
    public Recipe(String recipeName, List<RecipeIngredient> recipeItems, float prepTime,
                  float cookTime, float numberServings, String typeServing, String method) {
        this._recipeName = recipeName;
        this.recipeItems = recipeItems;
        this.prepTime = prepTime;
        this.cookTime = cookTime;
        this.numberServings = numberServings;
        this.typeServing = typeServing;
        this.method = method;
        setCost();
    }

    // Blank Constructor
    public Recipe() {
        this._recipeName = "";
        this.recipeItems = null;
        this.prepTime = 0;
        this.cookTime = 0;
        this.numberServings = 0;
        this.typeServing = "";
        this.method = "";
    }

    // "From Database" Constructor
    public Recipe(Map<String, Object> recipeMap) {
        this._recipeName = (String) recipeMap.get("recipeName");
        this.recipeItems = RecipeIngredient.getRecipeItemList((List<Object>) recipeMap.get("recipeItems"));
        this.prepTime = ((Number) recipeMap.get("prepTime")).floatValue();
        this.cookTime = ((Number) recipeMap.get("cookTime")).floatValue();
        this.cost = ((Number) recipeMap.get("cost")).floatValue();
        this.numberServings = ((Number) recipeMap.get("numberServings")).floatValue();
        this.typeServing = (String) recipeMap.get("typeServing");
        this.method = (String) recipeMap.get("method");
    }

    // Turns a Recipe object into a map, so that the recipeIngredients don't contain ingredient and
    // Unit objects, for saving to the Database.
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

    // Simplifies the recipeIngredient List so that it doesn't contain Ingredient and Unit objects.
    // There are custom toMap() functions for Ingredient and Unit.
    public List<Object> getSimplifiedRecipeIngredientList() {
        List<Object> myList = new ArrayList<>();
        for (RecipeIngredient recipeIngredient : this.getRecipeItems()) {
            myList.add(recipeIngredient.toMap());
        }
        return myList;
    }

    // Scales the recipe by a specified amount, increasing the quantities, and effectively the
    // price as well. Returns the new scaled recipe.
    public Recipe scaleRecipe(float scale) {
        //Recipe scaledRecipe = this;
        //scaledRecipe.numberServings = numberServings * scale;

        float numberOfServings = this.numberServings * scale;

        // Scale each of the recipeItems
        for(int i = 0; i < recipeItems.size();i++){
            recipeItems.get(i).setQuantity(scale);

            // Debugging
            Log.d("Name",recipeItems.get(i).getIngredient().getIngredientName());
            Log.d("Unit",recipeItems.get(i).getUnit().getUnitLabel());
            Log.d("Quantity",Float.toString(recipeItems.get(i).getQuantity()));
            Log.d("Multiplier",Float.toString(recipeItems.get(i).calcPrice() * scale));
        }
        Recipe scaledRecipe = new Recipe(this._recipeName,this.recipeItems,this.prepTime,this.cookTime,numberOfServings,this.typeServing,this.method);
        scaledRecipe.setCost(); // cost for the new scaled recipe
        return scaledRecipe;
    }

    public String getRecipeName() {
        return _recipeName;
    }

    public void setRecipeName(String recipeName) {
        this._recipeName = recipeName;
    }

    public List<RecipeIngredient> getRecipeItems() {
        return recipeItems;
    }

    public void setRecipeItems(List<RecipeIngredient> recipeItems) {
        this.recipeItems = recipeItems;
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

    // Calculates the cost of the recipe by looping through the recipeItem list and add the price
    // of each item.
    public void setCost() {
        float totalCost = 0;
        for (RecipeIngredient recipeIngredient : recipeItems) {
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



      // Debugging code
//    public void testRecipe(){
//        prepTime = 5;
//        RecipeIngredient ingredient1 = new RecipeIngredient("Eggs",3.0f,0.9f,"ml");
//        RecipeIngredient ingredient2 = new RecipeIngredient("bacon",1.0f,1.23f,"kg");
//        RecipeIngredient ingredient3 = new RecipeIngredient("bacon",3.4f,2.54f,"kg");
//        RecipeIngredient ingredient4 = new RecipeIngredient("bacon",2.3f,3.59f,"kg");
//        recipeItems.add(ingredient1);
//        recipeItems.add(ingredient2);
//        recipeItems.add(ingredient3);
//        recipeItems.add(ingredient4);
//        cookTime = 3;
//        cost = (float) 5.5;
//        numberServings = (float) 1.0;
//        typeServing = "Large";
//        method = "cook";
//    }
//



}
