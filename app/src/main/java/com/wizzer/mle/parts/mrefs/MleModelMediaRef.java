package com.wizzer.mle.parts.mrefs;

// Import Android classes.
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

// Import Magic Lantern Runtime Engine classes.
import com.wizzer.mle.runtime.MleTitle;
import com.wizzer.mle.runtime.ResourceManager;
import com.wizzer.mle.runtime.core.MleMediaRef;
import com.wizzer.mle.runtime.core.MleRuntimeException;

// Import Magic Lantern Parts classes.
import com.wizzer.mle.parts.j3d.MleJ3dPlatformData;
import com.wizzer.mle.parts.j3d.mrefs.IModelMediaRef;
import com.wizzer.mle.parts.j3d.min3d.Model;
import com.wizzer.mle.parts.j3d.min3d.ObjLoader;

import java.io.IOException;

/**
 * Created by msm on 8/16/16.
 */
public class MleModelMediaRef extends MleMediaRef implements IModelMediaRef
{
    /** Handle to Android resources. */
    protected Resources m_resources = null;
    /** Handle to Android <code>R</code> class for retrieving resource identifiers. */
    protected Class m_R = null;

    /**
     * The default constructor.
     */
    public MleModelMediaRef()
    {
        super();

        // Set the handle to the Android title resources.
        MleJ3dPlatformData platformData = (MleJ3dPlatformData)(MleTitle.getInstance().m_platformData);
        Context appContext = platformData.m_context;
        m_resources = appContext.getResources();
        m_R = platformData.m_R;
    }

    /**
     * Read the model data from a local file name.
     *
     * @return The loaded model is returned as a <code>Model</code>.
     *
     * @throws MleRuntimeException This exception is thrown if the
     * media reference data can not be successfully read.
     */
    public Model read()
        throws MleRuntimeException
    {
        Model model = null;

        // Get the data associated with the media reference.
        byte[] buffer = m_references.m_buffer;

        // Read Model from external reference.
        if (buffer != null)
    	{
            // Set the reference for the converter.
    		m_converter.setReference(buffer);

    		// Invoke the converter to prepare the local file.
    		String filename = m_converter.getFilename();

    		// true return means we downloaded successfully to a
    		// local file referred to by filename.
    		if (m_converter.conversionComplete())
    		{
    			try
    			{
    	            // Attempt to load the file from the resource cache.
    			    String subclassName = m_R.getName() + "$raw";
    			    Class subclass = Class.forName(subclassName);
    	            int id = ResourceManager.getResourceId(subclass, filename);
                    String rID = m_resources.getString(id);
                    ObjLoader loader = new ObjLoader(m_resources, rID, false);
                    model = loader.loadObject();
    			} catch (ClassNotFoundException ex)
    			{
    				Log.e(MleTitle.DEBUG_TAG, ex.getMessage());
    				model = null;
    			} catch (IOException ex)
                {
                    Log.e(MleTitle.DEBUG_TAG, ex.getMessage());
                    model = null;
                }
     		}
        }

        return model;
    }
}
