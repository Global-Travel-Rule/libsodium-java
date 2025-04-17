/*
 * Copyright (c) 2025 Global Travel Rule • globaltravelrule.com
 * License that can be found in the LICENSE file.
 *
 * Author: Global Travel Rule developer
 * Created on: 2025/3/19 23:38
 */

package com.globaltravelrule.sodium.loader;

import com.sun.jna.Native;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;

import static java.util.Objects.requireNonNull;

public class SharedLibraryLoader extends ResourceLoader {

    private final Object lock = new Object();


    private SharedLibraryLoader() {
        super();
    }

    /**
     * Get an instance of the loader.
     * @return Returns this loader instantiated.
     */
    public static SharedLibraryLoader get() {
        return SingletonHelper.INSTANCE;
    }

    public void loadSystemLibrary(String libraryName, List<Class<?>> classes) {
        registerLibraryWithClasses(libraryName, classes);
    }

    public void load(String resourceClasspath, Class<?> clazz) {
        load(resourceClasspath, Collections.singletonList(clazz));
    }

    public void load(String resourceClasspath, List<Class<?>> classes) {
        synchronized (lock) {
            try {
                File library = copyToTempDirectory(resourceClasspath, classes.get(0));
                setPermissions(library);
                if (library.isDirectory()) {
                    throw new IOException("Please supply a relative path to a file and not a directory.");
                }
                registerLibraryWithClasses(library.getAbsolutePath(), classes);
                requestDeletion(library);
            } catch (IOException e) {
                String message = String.format(
                        "Failed to load the bundled library from resources by classpath (%s)",
                        resourceClasspath
                );
                throw new ResourceLoaderException(message, e);
            } catch (URISyntaxException e) {
                String message = String.format(
                        "Finding the library from path (%s) failed!",
                        resourceClasspath
                );
                throw new ResourceLoaderException(message, e);
            }
        }
    }

    private void registerLibraryWithClasses(String absolutePath, List<Class<?>> classes) {
        requireNonNull(absolutePath, "Please supply an absolute path.");
        synchronized (lock) {
            for (Class<?> clazz : classes) {
                Native.register(clazz, absolutePath);
            }
        }
    }

    private static class SingletonHelper {
        private static final SharedLibraryLoader INSTANCE = new SharedLibraryLoader();
    }
}