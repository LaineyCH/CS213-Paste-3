package paste3.bakersbox;

public class RecipeIngredient {

    //Ingredient ingredient;
    float quantity;
    float atomicPrice;
    String name;
    String unit;
    //unit Unit;

    public RecipeIngredient(float quantity, float atomicPrice, String name, String unit){
        this.quantity = quantity;
        this.atomicPrice = atomicPrice;
        this.name = name;
        this.unit = unit;
    }


    public float calcPrice(){
        return quantity*=atomicPrice;
    }
}
