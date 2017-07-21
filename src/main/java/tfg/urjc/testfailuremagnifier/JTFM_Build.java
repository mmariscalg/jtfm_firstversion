/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tfg.urjc.testfailuremagnifier;

import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.AbstractProject;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Builder;
import java.io.IOException;
import jenkins.tasks.SimpleBuildStep;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;

/**
 *
 * @author mario.mariscal
 */
public class JTFM_Build extends Builder implements SimpleBuildStep{
    
    private final boolean task;
    
    // Fields in config.jelly must match the parameter names in the "DataBoundConstructor"
    @DataBoundConstructor
    public JTFM_Build(boolean task){
        this.task = task;
    }

    // We will use this from the <tt>config.jelly</tt>.
    public boolean getTask() {
        return task;
    }

    // Overridden for better type safety.
    // If your plugin doesn't really define any property on Descriptor,
    // you don't have to do this.
    @Override
    public JTFM_Build.Descriptor getDescriptor() {
        return (JTFM_Build.Descriptor)super.getDescriptor();
    }

    public void perform(Run<?, ?> run, FilePath fp, Launcher lnchr, TaskListener tl) throws InterruptedException, IOException {
                // This is where you 'build' the project.
        // Since this is a dummy, we just say 'hello world' and call that a build.
        // This also shows how you can consult the global configuration of the builder
        if (getDescriptor().getUseTest()){
            tl.getLogger().println("Use test");
            if (getTask())
                tl.getLogger().println("Task ON");
            else
                tl.getLogger().println("Task OFF");
        }
        else{
            tl.getLogger().println("Not Use test");
            if (getTask())
                tl.getLogger().println("Task ON");
            else
                tl.getLogger().println("Task OFF");
        }
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
        
        /**
         * In order to load the persisted global configuration, you have to 
         * call load() in the constructor.
         */
        public Descriptor() {
            load();
        }
        
        @Override
        public boolean isApplicable(Class<? extends AbstractProject> jobType) {
            return getUseTest();
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
        
         /**
         * This method returns true if the global configuration says we should speak French.
         *
         * The method name is bit awkward because global.jelly calls this method to determine
         * the initial state of the checkbox by the naming convention.
         */
        public boolean getUseTest() {
            return useTest;
        }
    }
    
    
}
