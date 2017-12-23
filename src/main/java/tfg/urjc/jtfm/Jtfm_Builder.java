package tfg.urjc.jtfm;
import hudson.Launcher;
import hudson.Extension;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;
import hudson.model.AbstractProject;
import hudson.tasks.Builder;
import hudson.tasks.BuildStepDescriptor;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;

/**
 * Sample {@link Builder}.
 *
 * 
 * When the user configures the project and enables this builder,
 * {@link DescriptorImpl#newInstance(StaplerRequest)} is invoked
 * and a new {@link Jtfm_Builder} is created. The created
 * instance is persisted to the project configuration XML by using
 * XStream, so this allows you to use instance fields (like {@link #name})
 * to remember the configuration.
 *
 * When a build is performed, the {@link #perform(AbstractBuild, Launcher, BuildListener)}
 * method will be invoked. 
 *
 * @author Kohsuke Kawaguchi
 */
public class Jtfm_Builder extends Builder {

    private final String task;

    // Fields in config.jelly must match the parameter task in the DataBoundConstructor
    @DataBoundConstructor
    public Jtfm_Builder(String task) {
        this.task = task;
    }

    //We will use this from the config.jelly.
    public String getTask() {
        return task;
    }

    @Override
    public boolean perform(AbstractBuild build, Launcher launcher, BuildListener listener) {
        // This is where project is build.

        // This also shows how you can consult the global configuration of the builder
        
        if (getDescriptor().getUseTestFM())
            System.out.println("Test-failure-magnifier project is build.");

        return true;
        
    }

    // Overridden for better type safety.
    // If your plugin does not really define any property on Descriptor,
    // you do not have to do this.
    @Override
    public DescriptorImpl getDescriptor() {
        return (DescriptorImpl)super.getDescriptor();
    }

    /**
     * Descriptor for {@link Jtfm_Builder}. Used as a singleton.
     * The class is marked as public so that it can be accessed from views.
     *
     * See src/main/resources/hudson/plugins/hello_world/Jtfm_Builder/*.jelly
     * for the actual HTML fragment for the configuration screen.
     */
    @Extension // This indicates to Jenkins that this is an implementation of an extension point.
    public static final class DescriptorImpl extends BuildStepDescriptor<Builder> {
        /**
         * To persist global configuration information,
         * simply store it in a field and call save().
         *
         * If you don't want fields to be persisted, use transient.
         */
        private boolean useTestFM;

        /**
         * Performs on-the-fly validation of the form field 'name'.
         *
         * @param aClass
         * @return
         *      Indicates the outcome of the validation. This is sent to the browser.
         */
//        public FormValidation doCheckName(@QueryParameter String value)
//                throws IOException, ServletException {
//            if (value.length() == 0)
//                return FormValidation.error("Please set a name");
//            if (value.length() < 4)
//                return FormValidation.warning("Isn't the name too short?");
//            return FormValidation.ok();
//        }

        @Override
        public boolean isApplicable(Class<? extends AbstractProject> aClass) {
            // Indicates that this builder can be used with all kinds of project types 
            return getUseTestFM();
        }

        /**
         * This human readable name is used in the configuration screen.
         * @return 
         */
        @Override
        public String getDisplayName() {
            return "jenkins-test-failure-magnifier";
        }

        @Override
        public boolean configure(StaplerRequest req, JSONObject formData) throws FormException {
            // To persist global configuration information,
            // set that to properties and call save().
            useTestFM = formData.getBoolean("useTestFM");
            // Can also use req.bindJSON(this, formData);
            // (easier when there are many fields; need set* methods for this, like setUseTestFM)
            save();
            return super.configure(req,formData);
        }

        /**
         * This method returns true if the global configuration says we should speak French.
         *
         * The method name is bit awkward because global.jelly calls this method to determine
         * the initial state of the checkbox by the naming convention.
         * @return 
         */
        public boolean getUseTestFM() {
            return useTestFM;
        }
    }
}

