package br.com.caelum.stella.nfe.builder.generated;import java.io.File;import java.io.FilenameFilter;import java.net.URISyntaxException;import java.net.URL;import java.util.ArrayList;import java.util.List;public class ClassEnumerator {    public List<Class<?>> getAllClassesInTheSamePackageAs(Class<?> type) throws ClassNotFoundException {        return listClassesInSamePackage(type, true);    }    private List<Class<?>> listClassesInSamePackage(Class<?> locator, boolean includeLocator)    throws ClassNotFoundException {        File packageFile = getPackageFile(locator);        String ignore = includeLocator ? null : locator.getSimpleName() + ".class";        return toClassList(locator.getPackage().getName(), listClassNames(packageFile, ignore));    }    private File getPackageFile(Class<?> locator) {        URL url = locator.getClassLoader().getResource(locator.getName().replace(".", "/") + ".class");        if (url == null) {            throw new RuntimeException("Cannot locate " + locator.getCanonicalName());        }        try {            return new File(url.toURI()).getParentFile();        } catch (URISyntaxException e) {            throw new RuntimeException(e);        }    }    private String[] listClassNames(File packageFile, final String ignore) {        return packageFile.list(new FilenameFilter() {            public boolean accept(File dir, String name) {                if (name.equals(ignore)) {                    return false;                }                return name.endsWith(".class");            }        });    }    private List<Class<?>> toClassList(String packageName, String[] classNames) throws ClassNotFoundException {        List<Class<?>> result = new ArrayList<Class<?>>(classNames.length);        for (String className : classNames) {            // Strip the .class            String simpleName = className.substring(0, className.length() - 6);            result.add(Class.forName(packageName + "." + simpleName));        }        return result;    }}