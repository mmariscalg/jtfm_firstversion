/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tfg.urjc.testfailuremagnifier;

import hudson.Extension;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.BuildListener;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Builder;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;

/**
 *
 * @author mario.mariscal
 */
public class JTFM_Build extends Builder{
    
    private final String task;
    
    // Fields in config.jelly must match the parameter names in the "DataBoundConstructor"
    @DataBoundConstructor
    private JTFM_Build(String task){
        this.task = task;
    }

    // We will use this from the <tt>config.jelly</tt>.
    public String getTask() {
        return task;
    }

    @Override
    public boolean perform(AbstractBuild build, Launcher launcher, BuildListener listener) {
        // This is where you 'build' the project.
        // Since this is a dummy, we just say 'hello world' and call that a build.
        // This also shows how you can consult the global configuration of the builder
        if (getDescriptor().getUseTest())
            listener.getLogger().println("Use test selected");
        else
            listener.getLogger().println("Use test does not selected");
        return true;
    }    
    
    // Overridden for better type safety.
    // If your plugin doesn't really define any property on Descriptor,
    // you don't have to do this.
    @Override
    public JTFM_Build.Descriptor getDescriptor() {
        return (JTFM_Build.Descriptor)super.getDescriptor();
    }
    
    @Extension
    public static class Descriptor extends BuildStepDescriptor<Builder> {
        /**
         * To persist global configuration information,
         * simply store it in a field and call save().
         *
         * <p>
         * If you don't want fields to be persisted, use <tt>transient</tt>.
         */
        private boolean useTest;
        
        @Override
        public boolean isApplicable(Class<? extends AbstractProject> jobType) {
            return true;
        }
 
        @Override
        public String getDisplayName() {
            return "Jenkins-test-failure-magnifier";
        }
                
        @Override
        public boolean configure(StaplerRequest req, JSONObject formData) throws FormException {
            // To persist global configuration information,
            // set that to properties and call save().
            useTest = formData.getBoolean("useTest");
            // ^Can also use req.bindJSON(this, formData);
            //  (easier when there are many fields; need set* methods for this, like setUseFrench)
            save();
            return super.configure(req,formData);
        }
        
        public boolean getUseTest() {
            return useTest;
        }
    }
    
    
}
