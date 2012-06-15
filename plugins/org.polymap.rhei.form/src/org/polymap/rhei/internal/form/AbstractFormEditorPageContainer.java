/* 
 * polymap.org
 * Copyright 2010, Falko Br�utigam, and other contributors as indicated
 * by the @authors tag.
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
 *
 * $Id: $
 */
package org.polymap.rhei.internal.form;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.opengis.feature.Property;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.core.runtime.NullProgressMonitor;

import org.polymap.rhei.field.FormFieldEvent;
import org.polymap.rhei.field.IFormField;
import org.polymap.rhei.field.IFormFieldListener;
import org.polymap.rhei.field.IFormFieldValidator;
import org.polymap.rhei.field.NullValidator;
import org.polymap.rhei.form.IFormEditorPage;
import org.polymap.rhei.form.IFormEditorPage2;
import org.polymap.rhei.form.IFormEditorPageSite;
import org.polymap.rhei.internal.DefaultFormFieldDecorator;
import org.polymap.rhei.internal.DefaultFormFieldLabeler;

/**
 * 
 *
 * @author <a href="http://www.polymap.de">Falko Br�utigam</a>
 */
public abstract class AbstractFormEditorPageContainer
        implements IFormEditorPageSite, IFormFieldListener {
    
    protected IFormEditorPage           page;
    
    private List<FormFieldComposite>    fields = new ArrayList();
    
    /** Listeners of type {@link IFormFieldListener}. */
    private ListenerList                listeners = new ListenerList( ListenerList.IDENTITY );
    
    
    public AbstractFormEditorPageContainer( IFormEditorPage page, String id, String title ) {
        this.page = page;
    }
    
    
    public synchronized void dispose() {
        if (page != null && page instanceof IFormEditorPage2) {
            ((IFormEditorPage2)page).dispose();
        }
        for (FormFieldComposite field : fields) {
            field.dispose();
        }
        fields.clear();
        listeners.clear();
    }

    
    public void addFieldListener( IFormFieldListener l ) {
        listeners.add( l );    
    }
    

    public void removeFieldListener( IFormFieldListener l ) {
        listeners.remove( l );    
    }

    
    /*
     * Called from page provider client code.
     */
    public void fireEvent( Object source, String fieldName, int eventCode, Object newValue ) {
        fieldChange( new FormFieldEvent( source, fieldName, null, eventCode, null, newValue ) );
    }
    
    
    
    public void fieldChange( FormFieldEvent ev ) {
// XXX a event scope is needed when registering for listener for field to distinguish
// between local event within that field or changes from other fields in the page or whole form
        
//        // propagate event to all fields
//        for (FormFieldComposite field : fields) {
//            if (field.getFormField() != ev.getFormField()) {
//                field.fireEvent( ev.getEventCode(), ev.getNewValue() );
//            }
//        }
        
        for (Object l : listeners.getListeners()) {
            ((IFormFieldListener)l).fieldChange( ev );
        }
    }


    public boolean isDirty() {
        if (page instanceof IFormEditorPage2) {
            if (((IFormEditorPage2)page).isDirty()) {
                return true;
            }
        }
        for (FormFieldComposite field : fields) {
            if (field.isDirty()) {
                return true;
            }
        }
        return false;
    }
    
    
    public boolean isValid() {
        if (page instanceof IFormEditorPage2) {
            if (!((IFormEditorPage2)page).isValid()) {
                return false;
            }
        }
        for (FormFieldComposite field : fields) {
            if (!field.isValid()) {
                return false;
            }
        }
        return true;
    }
    
    
    public Map<Property,Object> doSubmit( IProgressMonitor monitor )
    throws Exception {
        Map<Property,Object> result = new HashMap();
        
        for (FormFieldComposite field : fields) {
            if (field.isDirty()) {
                Object newValue = field.store();
                Object old = result.put( field.getProperty(), newValue );
                if (old != null) {
                    throw new RuntimeException( "Submitted value already exists for property: " + field.getProperty() );
                }
            }
        }

        // after form fields in order to allow subclassed Property instances
        // to be notified of submit
        if (page instanceof IFormEditorPage2) {
            ((IFormEditorPage2)page).doSubmit( monitor );
        }

        return result;
    }

    
    public void doLoad( IProgressMonitor monitor )
    throws Exception {
        if (page instanceof IFormEditorPage2) {
            ((IFormEditorPage2)page).doLoad( monitor );
        }

        ListenerList orig = listeners;
        try {
            // do not dispatch events while loading
            listeners = new ListenerList( ListenerList.IDENTITY );

            for (FormFieldComposite field : fields) {
                field.load();
            }
        }
        finally {
            listeners = orig;
        }
    }

    
    // IFormEditorPageSite ****************************
    
    public Composite newFormField( Composite parent, Property prop, IFormField field, IFormFieldValidator validator ) {
        return newFormField( parent, prop, field, validator, null );
    }


    public Composite newFormField( Composite parent, Property prop, IFormField field, IFormFieldValidator validator, String label ) {
        FormFieldComposite result = new FormFieldComposite( getToolkit(), prop, field,
                new DefaultFormFieldLabeler( label ), new DefaultFormFieldDecorator(), 
                validator != null ? validator : new NullValidator() );
        fields.add( result );
        
        result.addChangeListener( this );
        
        return result.createComposite( parent != null ? parent : getPageBody(), SWT.NONE );
    }

    
    public void setFieldValue( String fieldName, Object value ) {
        for (FormFieldComposite field : fields) {
            if (field.getFieldName().equals( fieldName )) {
                field.setFormFieldValue( value );
                return;
            }
        }
        throw new RuntimeException( "No such field: " + fieldName );
    }

    
    public void setFieldEnabled( String fieldName, boolean enabled ) {
        for (FormFieldComposite field : fields) {
            if (field.getFieldName().equals( fieldName )) {
                field.setEnabled( enabled );
                return;
            }
        }
        throw new RuntimeException( "No such field: " + fieldName );
        
    }

    
    public void reloadEditor()
    throws Exception {
        doLoad( new NullProgressMonitor() );
    }

}