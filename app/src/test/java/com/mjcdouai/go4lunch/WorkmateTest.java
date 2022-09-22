package com.mjcdouai.go4lunch;

import com.mjcdouai.go4lunch.model.Workmate;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class WorkmateTest {

    private Workmate mWorkmate;

    @Before
    public void setUp() {
        mWorkmate = new Workmate("mail@mail.com", "testUser", "photoUrl");
        mWorkmate.setDate("15/09/2022");
        mWorkmate.setChosenRestaurantId("chosenRestaurantId");
    }

    @Test
    public void getMail() {
        Assert.assertEquals(mWorkmate.getMail(), "mail@mail.com");
    }

    @Test
    public void getDate() {
        Assert.assertEquals(mWorkmate.getDate(), "15/09/2022");
    }

    @Test
    public void setDate() {
        mWorkmate.setDate("01/01/2035");
        Assert.assertEquals(mWorkmate.getDate(), "01/01/2035");
    }

    @Test
    public void getName() {
        Assert.assertEquals(mWorkmate.getName(), "testUser");
    }

    @Test
    public void getPhotoUrl() {
        Assert.assertEquals(mWorkmate.getPhotoUrl(), "photoUrl");
    }

    @Test
    public void getChosenRestaurantId() {
        Assert.assertEquals(mWorkmate.getChosenRestaurantId(), "chosenRestaurantId");
    }

    @Test
    public void setChosenRestaurantId() {
        mWorkmate.setChosenRestaurantId("chosenRestaurantId2");
        Assert.assertEquals(mWorkmate.getChosenRestaurantId(), "chosenRestaurantId2");

    }
}