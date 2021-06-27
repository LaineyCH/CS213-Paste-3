package paste3.bakersbox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Recipe {
    String _recipeName;
    List<RecipeIngredient> recipeItems;
    float prepTime;
    float cookTime;
    float cost;
    float numberServings;
    String typeServing;
    String method;

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
    public Recipe(Map<String, Object> ingredientmap) {
        this._recipeName = (String) ingredientmap.get("recipeName");
        this.recipeItems = RecipeIngredient.getRecipeItemList((List<Object>) ingredientmap.get("recipeItems"));
        this.prepTime = ((Number) ingredientmap.get("prepTime")).floatValue();
        this.cookTime = ((Number) ingredientmap.get("cookTime")).floatValue();
        this.cost = ((Number) ingredientmap.get("cost")).floatValue();
        this.numberServings = ((Number) ingredientmap.get("numberServings")).floatValue();
        this.typeServing = (String) ingredientmap.get("typeServing");
        this.method = (String) ingredientmap.get("method");
    }

    public Map<String, Object> toMap() {
        Map<String, Object> recipeMap = new HashMap<>();
        recipeMap.put("recipeName", _recipeName);
        recipeMap.put("recipeItems", getSimplifiedRecipeIngredientList());
        recipeMap.put("prepTime", prepTime);
        recipeMap.put("cookTime", cookTime);
        recipeMap.put("cost", cost);
        recipeMap.put("numberServings", numberServings);
        recipeMap.put("typeServing", typeServing);
        recipeMap.put("method", method);

        return recipeMap;
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

    public List<Object> getSimplifiedRecipeIngredientList() {
        List<Object> myList = new ArrayList<>();
        for (RecipeIngredient recipeIngredient : this.getRecipeItems()) {
           myList.add(recipeIngredient.toMap());
        }
        return myList;
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

    public void setCost(float cost) {
        this.cost = cost;
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


    /*
    public void testRecipe(){
        prepTime = 5;
        RecipeIngredient ingredient1 = new RecipeIngredient("Eggs",3.0f,0.9f,"ml");
        RecipeIngredient ingredient2 = new RecipeIngredient("bacon",1.0f,1.23f,"kg");
        RecipeIngredient ingredient3 = new RecipeIngredient("bacon",3.4f,2.54f,"kg");
        RecipeIngredient ingredient4 = new RecipeIngredient("bacon",2.3f,3.59f,"kg");
        recipeItems.add(ingredient1);
        recipeItems.add(ingredient2);
        recipeItems.add(ingredient3);
        recipeItems.add(ingredient4);
        cookTime = 3;
        cost = (float) 5.5;
        numberServings = (float) 1.0;
        typeServing = "Large";
        method = "cook";
    }

    public void scaleRecipe() {
        int recipeScale = 2;
        numberServings *= recipeScale;
        for(int i = 0; i < recipeItems.size();i++){
            recipeItems.get(i).quantity*=recipeScale;
            Log.d("Name",recipeItems.get(i).recipeIngredientName);
            Log.d("Unit",recipeItems.get(i).unit.getUnitLabel());
            Log.d("Quantity",Float.toString(recipeItems.get(i).quantity));
            Log.d("Atomic Price",Float.toString(recipeItems.get(i).atomicPrice));
            Log.d("Multiplier",Float.toString(recipeItems.get(i).calcPrice()*recipeScale));
        }
    }

 */
}
