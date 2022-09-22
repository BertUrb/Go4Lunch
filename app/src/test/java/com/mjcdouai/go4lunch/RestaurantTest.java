package com.mjcdouai.go4lunch;

import com.mjcdouai.go4lunch.model.Restaurant;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class RestaurantTest {

    private Restaurant mRestaurant;

    @Before
    public void setup() {
        mRestaurant = new Restaurant("restaurantId",
                "restaurantName",
                "restaurantAddress",
                true,
                20,
                30);
    }

    @Test
    public void getIdWithSuccess() {
        Assert.assertEquals("restaurantId", mRestaurant.getId());
    }

    @Test
    public void getNameWithSuccess() {
        Assert.assertEquals("restaurantName", mRestaurant.getName());
    }

    @Test
    public void getAddressWithSuccess() {
        Assert.assertEquals("restaurantAddress", mRestaurant.getAddress());
    }

    @Test
    public void getIsOpenWithSuccess() {
        Assert.assertTrue(mRestaurant.isOpen());
    }

    @Test
    public void getLongitudeWithSuccess() {
        Assert.assertEquals(30, mRestaurant.getLongitude(), 0);
    }

    @Test
    public void getLatitudeWithSuccess() {
        Assert.assertEquals(20, mRestaurant.getLatitude(), 0);
    }
}
