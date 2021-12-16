package ru.annachemic.tests;


import com.github.javafaker.Faker;
import okhttp3.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import retrofit2.Retrofit;
import ru.annachemic.dto.Category;
import ru.annachemic.dto.Product;
import ru.annachemic.enums.CategoryType;
import ru.annachemic.service.CategoryService;
import ru.annachemic.service.ProductService;
import ru.annachemic.utils.PrettyLogger;
import ru.annachemic.utils.RetrofitUtils;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class ProductTests {
    static Retrofit client;
    static ProductService productService;
    static CategoryService categoryService;
    Faker faker = new Faker();
    Product product;


    @BeforeAll
    static void beforeAll() {
        client = RetrofitUtils.getRetrofit();
        productService = client.create(ProductService.class);
        categoryService = client.create(CategoryService.class);
    }

    @BeforeEach
    void setUp() {
        product = new Product()
                .withTitle(faker.food().sushi())
                .withPrice((int) ((Math.random() + 1) * 10000))
                .withCategoryTitle(CategoryType.FOOD.getTitle())
                .withCategoryTitle(CategoryType.ELECTRONIC.getTitle())
                .withCategoryTitle(CategoryType.FURNITURE.getTitle());
    }

    @Test
    void postProductTest() throws IOException {

        Response<Product> response = productService.createProduct(product).execute();

        assertThat(response.body().getTitle(), equalTo(product).getTitle());
        assertThat(response.body().getPrice(), equalTo(product).getPrice());
        assertThat(response.body().getCategory(), equalTo(product).getCategory());
    }
    @Test
    void getCategoryByIdTest() throws IOException {
        Integer id = CategoryType.ELECTRONIC.getId();

        Response<Category> response = categoryService
                .getCategory(id)
                .execute();

        PrettyLogger.DEFAULT.log(response.body().toString());

        assertThat(response.body().getTitle(), equalTo(CategoryType.ELECTRONIC.getTitle()));
        assertThat(response.body().getId(), equalTo(id));
    }
}
