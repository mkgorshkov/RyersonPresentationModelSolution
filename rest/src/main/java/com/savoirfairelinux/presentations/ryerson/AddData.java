package com.savoirfairelinux.presentations.ryerson;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

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
@Path("/quote")
public class AddData {

    // When we have a new file we need to add headers.
    boolean newFile = false;

    /**
     * We can add new lines directly to our CSV file. In order to avoid putting all of the data in a database and then
     * thinking of a way to retrieve the data, all users share the same file across the network, as found on the master
     * machine.
     *
     * Please refer to the specifics of the parameters in the presentation or portlet document.
     *
     * Improvements: Usually there is some verification of a secret key in order to avoid attacks. In this case we just
     * have a secret string.
     *
     * @param firstName
     * @param lastName
     * @param gender
     * @param postalCode
     * @param dateOfBirth
     * @param vehicleType
     * @param make
     * @param model
     * @param year
     * @param condition
     * @return
     */
    @GET
    @Path("/add/{firstName}/{lastName}/{gender}/{postalCode}/{dateOfBirth}/{vehicleType}/{make}/{model}/{year}/{condition}/{secret}")
    public Response addQuoteRequest(@PathParam("firstName") String firstName, @PathParam("lastName") String lastName, @PathParam("gender") String gender,
                                    @PathParam("postalCode") String postalCode, @PathParam("dateOfBirth") String dateOfBirth, @PathParam("vehicleType") String vehicleType,
                                    @PathParam("make") String make, @PathParam("model") String model, @PathParam("year") String year, @PathParam("condition") String condition,
                                    @PathParam("secret") String secret){

        // Simple check to make sure that we're sending over a security key
        if(!secret.equals("X-SECRET-RYERSON")){
            return Response.status(Response.Status.UNAUTHORIZED).entity("Unauthorized.").build();
        }

        /*
        Before we do anything, we need to make sure that the file exists. If it doesn't, fill it with headers.
         */
        File f = new File(RestUtils.OUTPUT_FILE);
        if(!f.exists()){
            newFile = true;
            try {
                f.createNewFile();
            } catch (IOException e) {
                System.err.println("Error: Cannot create a new .CSV file");
                e.printStackTrace();
                return Response.status(Response.Status.BAD_REQUEST).entity("Cannot create a new CSV file.").build();            }
        }


        BufferedWriter bw = null;
        FileWriter fw = null;

        try{
            StringBuilder sb = new StringBuilder();

            if(newFile){
                sb.append(RestUtils.csvFiyHeaders());
            }

            // Straight forward data types that don't need extra formatting
            sb.append(RestUtils.csvFiy(firstName));
            sb.append(RestUtils.csvFiy(lastName));
            sb.append(RestUtils.csvFiy(gender));
            sb.append(RestUtils.csvFiy(postalCode));

            // Normally, we would want to convert at this end but we already sent the date String properly formatted
            sb.append(RestUtils.csvFiy(dateOfBirth));

            // Althought these will be encoded as strings, upon importing the program should recognize them as necessary
            sb.append(RestUtils.csvFiy(vehicleType));
            sb.append(RestUtils.csvFiy(make));
            sb.append(RestUtils.csvFiy(model));
            sb.append(RestUtils.csvFiy(year));

            // Last one should be different not to have thje trailing ,
            sb.append(RestUtils.csvFiyEnding(condition));

            fw = new FileWriter(RestUtils.OUTPUT_FILE);
            bw = new BufferedWriter(fw);

            bw.append(sb.toString());

        } catch(IOException e){
            System.err.println("Error: Cannot write to .CSV file");
            e.printStackTrace();
            return Response.status(Response.Status.BAD_REQUEST).entity("Canot write to CSV file.").build();

        } finally{

            try {
                if (bw != null) {
                    bw.close();
                }
                if (fw != null) {
                    fw.close();
                }
            }catch (IOException e){
                System.err.println("Error: Cannot close streams");
                e.printStackTrace();
                return Response.status(Response.Status.BAD_REQUEST).entity("Cannot close streams. The data was most likely ok though.").build();
            }

        }

        return Response.status(200).entity("Sent successfully!").build();
    }

}
