/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0 and the Server Side Public License, v 1; you may not use this file except
 * in compliance with, at your election, the Elastic License 2.0 or the Server
 * Side Public License, v 1.
 */

package org.plainpicture.sorting;

import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.index.PostingsEnum;
import org.apache.lucene.index.Term;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.plugins.Plugin;
import org.elasticsearch.plugins.ScriptPlugin;
import org.elasticsearch.script.NumberSortScript;
import org.elasticsearch.script.NumberSortScript.LeafFactory;
import org.elasticsearch.script.ScriptContext;
import org.elasticsearch.script.ScriptEngine;
import org.elasticsearch.search.lookup.SearchLookup;
import org.plainpicture.sorting.SortScript;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.script.ScriptEngineFactory;

/**
 * An example script plugin that adds a {@link ScriptEngine}
 * implementing scoring.
 */
public class SortScriptPlugin extends Plugin implements ScriptPlugin {

    @Override
    public ScriptEngine getScriptEngine(
        Settings settings,
        Collection<ScriptContext<?>> contexts
    ) {
        return new SortScriptEngine();
    }
    private static class SortScriptEngine implements ScriptEngine {
        @Override
        public String getType() {
            return "sort_scripts";
        }

        @Override
        public <T> T compile(
            String scriptName,
            String scriptSource,
            ScriptContext<T> context,
            Map<String, String> params
        ) {
            System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX: "+SortScript.CONTEXT.name);
            if (context.equals(SortScript.CONTEXT) == false) {
                throw new IllegalArgumentException(getType()
                        + " scripts cannot be used for context ["
                        + context.name + "]");
            }
            if ("sort_script".equals(scriptSource)) {
                System.out.println("XXXXXXXXXXXXXX: In Zeile 66");
                SortScript.Factory factory = new PureSortScriptFactory();
                return context.factoryClazz.cast(factory);
             }
             throw new IllegalArgumentException("Unknown script name "
                     + scriptSource);
        }

        @Override
        public void close() {
            // optionally close resources
        }



        private static class PureSortScriptFactory implements NumberSortScript.Factory, ScriptEngineFactory {


            @Override
            public LeafFactory newFactory(
                Map<String, Object> params,
                SearchLookup lookup
            ) {
                return new PureSortScriptLeafFactory(params, lookup);
            }

            @Override
            public String getEngineName() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public String getEngineVersion() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public List<String> getExtensions() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public String getLanguageName() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public String getLanguageVersion() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public String getMethodCallSyntax(String arg0, String arg1, String... arg2) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public List<String> getMimeTypes() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public List<String> getNames() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public String getOutputStatement(String arg0) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public Object getParameter(String arg0) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public String getProgram(String... arg0) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public javax.script.ScriptEngine getScriptEngine() {
                // TODO Auto-generated method stub
                return null;
            }
        }

        private static class PureSortScriptLeafFactory implements LeafFactory {
            private final Map<String, Object> params;
            private final SearchLookup lookup;
            private final String field;
            private final String term;

            private PureSortScriptLeafFactory(
                        Map<String, Object> params, SearchLookup lookup) {
                System.out.println("XXXXXXXParams: " + params);
                System.out.println("XXXXXXXLookup: " + lookup);
                this.params = params;
                this.lookup = lookup;
                field = ""; //params.get("field").toString();
                term = " "; //params.get("term").toString();
            }

            @Override
            public boolean needs_score() {
                return false;  // Return true if the script needs the score
            }

            @Override
            public NumberSortScript newInstance(LeafReaderContext ctx) throws IOException {
                System.out.println("New Instance");

                // TODO Auto-generated method stub
                return new SortScript(params, lookup, ctx);
            }
        }
    }
    // end::sort_engine
}