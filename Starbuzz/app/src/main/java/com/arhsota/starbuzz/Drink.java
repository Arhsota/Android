package com.arhsota.starbuzz;

public class Drink {
    private String name;
    private String description;
    private int imageResourceId;

    //drinks — массив с элементами Drink
     public static final Drink[] drinks = {
            new Drink("Latte", "A couple of espresso shots with steamed milk", R.drawable.latte),
            new Drink("Cappuccino", "Espresso, hot milk, and a steamed milk foam",  R.drawable.cappuccino),
            new Drink("Filter", "Highest quality beans roasted and brewed fresh",   R.drawable.filter)
     };

    //Для каждого напитка хранится имя, описание и ресурс изображения
//    Это конструктор
       private Drink(String name, String description, int imageResourceId) {
          this.name = name;
          this.description = description;
          this.imageResourceId = imageResourceId;
       }

//                Get-методы
          public String getDescription() {
          return description;
       }

           public String getName() {
           return name;
       }
           public int getImageResourceId() {
           return imageResourceId;
       }
// в качестве строкового представления Drink использует название напитка
           public String toString() {
           return this.name;
       }


}
