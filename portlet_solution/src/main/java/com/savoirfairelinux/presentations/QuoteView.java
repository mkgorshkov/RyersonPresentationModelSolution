package com.savoirfairelinux.presentations;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.server.Page;
import com.vaadin.ui.Notification;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * %%
 * Copyright (C) 2017 SFL
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

public class QuoteView extends Quote{

    /*
     One fieldgroup per page in the tabsheet - allows us to perform validation on each of the fields without
     having to go through each one. We can also retrieve the data in groups for when we do the REST calls rather than
     one at a time.
      */
    private FieldGroup personalGroup = new FieldGroup();
    private FieldGroup vehicleGroup = new FieldGroup();


    public QuoteView(){
        bindData();
        addValidators();
        addClickListeners();
    }

    /**
        Go through each of the fields that we have on each page in the tabsheet and add them to the respective FieldGroup.
     */
    private void bindData(){
        personalGroup.bind(firstName, 0);
        personalGroup.bind(lastName, 1);
        personalGroup.bind(gender, 2);
        personalGroup.bind(postalCode, 3);
        personalGroup.bind(dateOfBirth, 4);

        vehicleGroup.bind(vehicleType, 0);
        vehicleGroup.bind(make, 1);
        vehicleGroup.bind(model, 2);
        vehicleGroup.bind(year, 3);
        vehicleGroup.bind(condition, 4);
    }

    /**
     * Validators allow us to define the range of values which we deem are acceptable.
     * Can you think of some validators that we can add to make our example more realistic?
     */
    private void addValidators(){
        postalCode.addValidator(new RegexpValidator("^[A-Za-z]\\d[A-Za-z][ -]?\\d[A-Za-z]\\d", "Please enter valid postal code."));
    }

    /**
     * Listeners allow us to attach actions to different events. For example, a value change listener is triggered
     * when a UI component such a drop down menu has a change in its value. Or, a click listener is triggered when
     * an action is performed on a button.
     */
    private void addClickListeners(){

        // We basically indicate that aside from the initial setup, we can't have a null or empty value, avoids things
        // like null pointer exceptions.
        vehicleType.setNullSelectionAllowed(false);
        make.setNullSelectionAllowed(false);
        model.setNullSelectionAllowed(false);

        /*
        Slightly quick and dirty, and in terms of space and time complexity, it isn't ideal:
            We have detected that we switched to an item
                Remove all existing data in the dropdown boxes
                Add appropriate data to the boxes
                Make sure that the boxes are modifiable
         */
        vehicleType.addValueChangeListener(valueChangeEvent -> {
                if(valueChangeEvent.getProperty().getValue().equals("Motorcycle")) {
                    make.removeAllItems();
                    make.addItems(VehicleData.MOTORCYCLE_MAKE);
                    make.setEnabled(true);
                    model.removeAllItems();
                    model.addItems(VehicleData.UNIVERSAL_MODEL);
                    model.setEnabled(true);
                    condition.setEnabled(true);
                    year.setEnabled(true);
                }
                else if(valueChangeEvent.getProperty().getValue().equals("Automobile")) {
                    make.removeAllItems();
                    make.addItems(VehicleData.AUTO_MAKE);
                    make.setEnabled(true);
                    model.removeAllItems();
                    model.addItems(VehicleData.UNIVERSAL_MODEL);
                    model.setEnabled(true);
                    condition.setEnabled(true);
                    year.setEnabled(true);
                }
            });

        // We only allow a user to click next if we validated the current form. Otherwise, we should throw an error.
        next.addClickListener(e -> {
            if(personalGroup.isValid()){
                setEnabled(true);
                setSelectedTab(1);
            }else{
                new Notification("Required Fields", "Please check that all fields are filled correctly", Notification.Type.ERROR_MESSAGE).show(Page.getCurrent());
            }
        });

        // We only allow a user to click submit if we validated both of the forms. Otherwise, we should throw an error.
        submit.addClickListener(e ->{
            if(personalGroup.isValid() && vehicleGroup.isValid()){
                setUpREST();
            }else{
                new Notification("Required Fields", "Please check that all fields are filled correctly", Notification.Type.ERROR_MESSAGE).show(Page.getCurrent());
            }
        });
    }

    /**
     * We have a REST API ready to accept the quotes and will compile the data in order to give us a large
     * data set.
     *
     * To review the slides, in order to send a request we need to call:
     * http://{IP}:8080/rest/quote/add/{ADD ALL PARAMETERS ONE BY ONE + SECRET CODE}
     *
     * In order to see the results, we have to call:
     * http://{IP}:8080/rest/datastore/getData/{SECRET-CODE}
     *
     *
     **/
    private void setUpREST(){
        StringBuilder sbURL = new StringBuilder();

        sbURL.append(RestUtils.SEND_QUOTE);

        for(int i = 0; i < personalGroup.getBoundPropertyIds().size(); i++){
            sbURL.append("/");
            sbURL.append(personalGroup.getField(i));
        }

        for(int i = 0; i < vehicleGroup.getBoundPropertyIds().size(); i++){
            sbURL.append("/");
            sbURL.append(vehicleGroup.getField(i));
        }

        sbURL.append("/");
        sbURL.append(RestUtils.SECRET_KEY);

        try{
            // URL cannot have a space, so we need to encode it properly

            String encodedURL = sbURL.toString().replace(" ", "%20");

            URL connectionURL = new URL(encodedURL);
            HttpURLConnection conn = (HttpURLConnection) connectionURL.openConnection();

            conn.setRequestMethod("GET");

            if(conn.getResponseCode() != 200){
                System.err.println("200 error - something went wrong.");
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

            String output;
            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                System.out.println(output);
            }

            conn.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
