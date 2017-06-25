package semexe.servlet;

public interface BasketFactory {
    BasketItem newBasketItem(Item parent, String name, String sourcePath);
}
