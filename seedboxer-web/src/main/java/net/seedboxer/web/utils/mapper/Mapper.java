/*******************************************************************************
 * Mapper.java
 * 
 * Copyright (c) 2012 SeedBoxer Team.
 * 
 * This file is part of SeedBoxer.
 * 
 * SeedBoxer is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * SeedBoxer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with SeedBoxer.  If not, see <http ://www.gnu.org/licenses/>.
 ******************************************************************************/
package net.seedboxer.web.utils.mapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.seedboxer.web.utils.mapper.annotation.MapToObject;

import org.modelmapper.ModelMapper;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.classreading.SimpleMetadataReaderFactory;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.util.ClassUtils;
import org.springframework.util.SystemPropertyUtils;

/**
 * @author Jorge Davison (jdavisonc)
 *
 */
public class Mapper {
	
    private final Class<MapToObject> MAPPER_ANNOTATION = MapToObject.class;
    private static final String CLASS_RESOURCE_PATTERN = "**/*.class";

    private ResourcePatternResolver resourcePatternResolver;
    private final MetadataReaderFactory metadataReaderFactory = new SimpleMetadataReaderFactory();
    private final TypeFilter annotationFilter = new AnnotationTypeFilter(MAPPER_ANNOTATION);
	
	private List<String> packages;
	
	private Map<Class<?>, Class<?>> mappings;
	
	private final ModelMapper modelMapper = new ModelMapper();
	
	public void init() throws ClassNotFoundException {
        if (packages != null) {
        	mappings = new HashMap<Class<?>, Class<?>>();
        	List<Class<?>> annotated = new ArrayList<Class<?>>();

            try {
                for (String p : packages) {
                    String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
                            ClassUtils.convertClassNameToResourcePath(SystemPropertyUtils.resolvePlaceholders(p)) +
                            "/" +
                            CLASS_RESOURCE_PATTERN;

                    Resource[] resources = resourcePatternResolver.getResources(packageSearchPath);

                    for (Resource resource : resources) {
                        if (resource.isReadable()) {
                            MetadataReader metadataReader = this.metadataReaderFactory.getMetadataReader(resource);

                            if (this.annotationFilter.match(metadataReader, metadataReaderFactory)) {
                            	annotated.add(Class.forName(metadataReader.getAnnotationMetadata().getClassName()));
                            }
                        }
                    }
                }
            } catch (IOException ex) {
                throw new RuntimeException("I/O failure during classpath scanning", ex);
            }
            
            for (Class<?> clazz : annotated) {
				Class<?> toMap = clazz.getAnnotation(MAPPER_ANNOTATION).value();
				
				mappings.put(clazz, toMap);
			}
        }
	}
	
	public Object map(Object toMap) {
		
		if (toMap == null) {
			return null;
		}
		
		Class<?> mappedClass = mappings.get(toMap.getClass());
		if (mappedClass != null) {
			return modelMapper.map(toMap, mappedClass);			
		} else {
			return toMap;
		}
	}

}
