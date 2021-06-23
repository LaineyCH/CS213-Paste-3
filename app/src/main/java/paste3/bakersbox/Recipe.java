package paste3.bakersbox;

import android.util.Log;

import java.util.ArrayList;

public class Recipe {
    String name;
    ArrayList<RecipeIngredient> recipeItems = new ArrayList<RecipeIngredient>();
    float prepTime;
    float cookTime;
    float cost;
    float numberServings;
    String typeServing;
    String method;

    public void testRecipe(){
        prepTime = 5;
        RecipeIngredient ingredient1 = new RecipeIngredient(3.0f,0.9f,"Eggs","ml");
        RecipeIngredient ingredient2 = new RecipeIngredient(1.0f,1.23f,"bacon","kg");
        RecipeIngredient ingredient3 = new RecipeIngredient(3.4f,2.54f,"bacon","kg");
        RecipeIngredient ingredient4 = new RecipeIngredient(2.3f,3.59f,"bacon","kg");
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
            Log.d("Name",recipeItems.get(i).name);
            Log.d("Unit",recipeItems.get(i).unit);
            Log.d("Quantity",Float.toString(recipeItems.get(i).quantity));
            Log.d("Atomic Price",Float.toString(recipeItems.get(i).atomicPrice));
            Log.d("Multiplier",Float.toString(recipeItems.get(i).calcPrice()*recipeScale));
        }
    }
}
