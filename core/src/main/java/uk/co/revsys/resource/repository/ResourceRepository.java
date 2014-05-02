/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.co.revsys.resource.repository;

import uk.co.revsys.resource.repository.model.Directory;
import uk.co.revsys.resource.repository.model.RepositoryItem;
import uk.co.revsys.resource.repository.model.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 *
 * @author Adam
 */
public interface ResourceRepository {
	
	public void write(Resource resource, InputStream inputStream) throws IOException;
	
	public InputStream read(Resource resource) throws IOException;
	
	public void delete(Resource resource) throws IOException;
	
	public void delete(Directory directory) throws IOException;
	
	public List<RepositoryItem> list(String path) throws IOException;
	
	public List<Directory> listDirectories(String path) throws IOException;
	
	public List<Resource> listResources(String path) throws IOException;
	
}
