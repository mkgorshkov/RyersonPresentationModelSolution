package com.savoirfairelinux.presentations.ryerson;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.io.BufferedReader;
import java.io.FileReader;
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
@Path("/datastore")
public class GetAllData {

    /**
     * Allows us to retrieve the collected data from the presentation. This gives the participants some data to play with,
     * and to continue in learning about the Vaadin + Liferay platforms.
     *
     * Improvements: As mentioned, there should be a better authentication process than a simple secret key.
     *
     * @param secret
     * @return
     */
    @GET
    @Path("/getData/{secret}")
    public Response getData(@PathParam("secret") String secret){

        if(!secret.equals("X-SECRET-RYERSON")){
            return Response.status(Response.Status.UNAUTHORIZED).entity("Unauthorized.").build();
        }

        StringBuilder sb = new StringBuilder();

        try {
            BufferedReader in = new BufferedReader(new FileReader(RestUtils.OUTPUT_FILE));

            String sCurrentLine;
            while ((sCurrentLine = in.readLine()) != null) {
                sb.append(sCurrentLine);
            }


            in.close();

        } catch (IOException e) {
            System.err.println("Error: Cannot read from .CSV file");
            e.printStackTrace();
            return Response.status(Response.Status.BAD_REQUEST).entity("Cannot read from CSV file.").build();
        }

        return Response.status(200).entity(sb.toString()).build();
    }
}
