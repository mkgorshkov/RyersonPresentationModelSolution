package com.savoirfairelinux.presentations;

import java.util.ArrayList;
import java.util.Arrays;

/**
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
public class VehicleData {
    // Some fake data I created to be used in the form. I'm sure you can find a better way to get this data, for example a database...
    final static ArrayList<String> MOTORCYCLE_MAKE = new ArrayList<>(Arrays.asList("Suzuki", "Yamaha", "Honda", "Ducati"));
    final static ArrayList<String> AUTO_MAKE = new ArrayList<>(Arrays.asList("Toyota","BMW","Audi","Mercedes"));
    final static ArrayList<String> UNIVERSAL_MODEL = new ArrayList<>(Arrays.asList("Model A", "Model B", "Model C", "Model D"));
}
