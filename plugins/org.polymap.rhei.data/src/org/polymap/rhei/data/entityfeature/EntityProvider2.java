/* 
 * polymap.org
 * Copyright 2011-2013, Falko Br�utigam. All rights reserved.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 */
package org.polymap.rhei.data.entityfeature;

import org.geotools.data.Query;
import org.opengis.feature.Feature;
import org.opengis.feature.type.FeatureType;

import org.polymap.core.model.Entity;

/**
 * In addition to the {@link EntityProvider} this interface also provides the
 * {@link FeatureType} to be used and it builds the actual {@link Feature}
 * instances that are used to represent the entities.
 * 
 * @see EntityProvider3
 * @author <a href="http://www.polymap.de">Falko Br�utigam</a>
 */
public interface EntityProvider2<T extends Entity>
        extends EntityProvider<T> {

    public FeatureType buildFeatureType();
    
    public Feature buildFeature( Entity entity, FeatureType schema );

    public void modifyFeature( Entity entity, String propName, Object value )
    throws Exception;

    public Query transformQuery( Query query );
    
}
