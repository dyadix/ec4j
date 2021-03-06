/**
 * Copyright (c) 2017 Angelo Zerr and other contributors as
 * indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ec4j.core;

import java.io.IOException;

import org.ec4j.core.model.EditorConfig;
import org.ec4j.core.model.Version;
import org.ec4j.core.parser.EditorConfigModelHandler;
import org.ec4j.core.parser.EditorConfigParser;
import org.ec4j.core.parser.ErrorHandler;

/**
 * Implements the capability of loading an {@link EditorConfig} object out of a {@link Resource}.
 *
 * @author <a href="https://github.com/ppalaga">Peter Palaga</a>
 */
public class EditorConfigLoader {

    /**
     * @return a new {@link EditorConfigLoader} with {@link PropertyTypeRegistry#default_()}, {@link Version#CURRENT}
     *         and {@link ErrorHandler#THROW_SYNTAX_ERRORS_IGNORE_OTHERS}
     */
    public static EditorConfigLoader default_() {
        return new EditorConfigLoader(new EditorConfigModelHandler(PropertyTypeRegistry.default_(), Version.CURRENT),
                ErrorHandler.THROW_SYNTAX_ERRORS_IGNORE_OTHERS);
    }

    public static EditorConfigLoader of(Version version) {
        return of(version, PropertyTypeRegistry.default_());
    }

    public static EditorConfigLoader of(Version version, PropertyTypeRegistry registry) {
        return of(version, registry, ErrorHandler.THROW_SYNTAX_ERRORS_IGNORE_OTHERS);
    }

    public static EditorConfigLoader of(Version version, PropertyTypeRegistry registry, ErrorHandler errorHandler) {
        return new EditorConfigLoader(new EditorConfigModelHandler(registry, version), errorHandler);
    }

    private final ErrorHandler errorHandler;

    private final EditorConfigModelHandler handler;
    private final EditorConfigParser parser;

    public EditorConfigLoader(EditorConfigModelHandler handler, ErrorHandler errorHandler) {
        super();
        this.parser = EditorConfigParser.default_();
        this.handler = handler;
        this.errorHandler = errorHandler;
    }

    /**
     * Loads an {@link EditorConfig} object out of the given {@code configFile}.
     *
     * @param configFile
     *            the {@link Resource} to read the EditorConfig model from
     * @return a new {@link EditorConfig} instance
     * @throws IOException
     *             on I/O problems during the reading from the given {@link Resource}
     */
    public EditorConfig load(Resource configFile) throws IOException {
        try {
            parser.parse(configFile, handler, errorHandler);
            EditorConfig result = handler.getEditorConfig();
            return result;
        } catch (IOException e) {
            throw new IOException("Could not load " + configFile.getPath(), e);
        }
    }
}