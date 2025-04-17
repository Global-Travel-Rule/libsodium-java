/*
 * Copyright (c) 2025 Global Travel Rule • globaltravelrule.com
 * License that can be found in the LICENSE file.
 *
 * Author: Global Travel Rule developer
 * Created on: 2025/3/19 23:38
 */

package com.globaltravelrule.sodium.loader;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.attribute.PosixFilePermission;
import java.util.HashSet;
import java.util.Set;

public class FileLoader extends ResourceLoader {

    private FileLoader() {
        super();
    }

    /**
     * Get an instance of the loader.
     * @return Returns this loader instantiated.
     */
    public static FileLoader get() {
        return SingletonHelper.INSTANCE;
    }


    /**
     * Load a file/directory from your resource folder using a relative path.
     * This will return your file or directory with global read, write and execute.
     * @param relativePath Relative path to your file or directory.
     * @return The file your directory.
     * @throws IOException If at any point processing of the resource file fails.
     * @throws URISyntaxException If cannot find the resource file.
     */
    public File load(String relativePath, Class<?> outsideClass) throws IOException, URISyntaxException {
        return load(relativePath, new HashSet<>(), outsideClass);
    }

    /**
     * Load a file/directory from your resource folder with permissions
     * you set. On windows, any type of read, write and execute permissions will
     * be set appropriately.
     * @param relativePath Relative path to your file or directory.
     * @param permissions A set of permissions.
     * @return The file your directory.
     * @throws IOException If at any point processing of the resource file fails.
     * @throws URISyntaxException If cannot find the resource file.
     */
    public File load(String relativePath, Set<PosixFilePermission> permissions, Class<?> outsideClass)
            throws IOException, URISyntaxException {
        return loadFromRelativePath(relativePath, permissions, outsideClass);
    }

    private File loadFromRelativePath(String relativePath, Set<PosixFilePermission> filePermissions, Class<?> outsideClass)
            throws IOException, URISyntaxException {
        File file = copyToTempDirectory(relativePath, outsideClass);
        setPermissions(file, filePermissions);
        return file;
    }

    private static class SingletonHelper {
        private static final FileLoader INSTANCE = new FileLoader();
    }
}