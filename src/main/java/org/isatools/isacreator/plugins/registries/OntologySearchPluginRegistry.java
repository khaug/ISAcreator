package org.isatools.isacreator.plugins.registries;

import org.isatools.isacreator.configuration.RecommendedOntology;
import org.isatools.isacreator.ontologymanager.OntologySourceRefObject;
import org.isatools.isacreator.ontologymanager.common.OntologyTerm;
import org.isatools.isacreator.plugins.host.service.PluginOntologyCVSearch;

import java.util.*;

/**
 * Created by the ISA team
 *
 * @author Eamonn Maguire (eamonnmag@gmail.com)
 *         <p/>
 *         Date: 02/10/2011
 *         Time: 22:23
 */
public class OntologySearchPluginRegistry {

    private static List<PluginOntologyCVSearch> ontologyCVSearchPlugins = new ArrayList<PluginOntologyCVSearch>();

    public static void registerPlugin(PluginOntologyCVSearch plugin) {
        System.out.println("Registering ontology search plugin");
        ontologyCVSearchPlugins.add(plugin);
    }

    public static void deregisterPlugin(PluginOntologyCVSearch plugin) {
        System.out.println("Deregistering ontology search plugin");
        if (ontologyCVSearchPlugins.contains(plugin)) {
            ontologyCVSearchPlugins.remove(plugin);
        }
    }

    public static Map<OntologySourceRefObject, List<OntologyTerm>> compositeSearch(String term) {
        Map<OntologySourceRefObject, List<OntologyTerm>> result = new HashMap<OntologySourceRefObject, List<OntologyTerm>>();

        for (PluginOntologyCVSearch searchResource : ontologyCVSearchPlugins) {
            result.putAll(searchResource.searchRepository(term));
        }

        return result;
    }

    public static Map<OntologySourceRefObject, List<OntologyTerm>> compositeSearch(String term, Map<String, RecommendedOntology> recommendedOntologies) {
        Map<OntologySourceRefObject, List<OntologyTerm>> result = new HashMap<OntologySourceRefObject, List<OntologyTerm>>();

        for (PluginOntologyCVSearch searchResource : ontologyCVSearchPlugins) {
            result.putAll(searchResource.searchRepository(term, recommendedOntologies, false));
        }

        return result;
    }

    public static List<PluginOntologyCVSearch> getOntologyCVSearchPlugins() {
        return ontologyCVSearchPlugins;
    }

    public static boolean areSearchResourcesAvailableForCurrentField(Map<String, RecommendedOntology> recommendedOntologies) {

        for (PluginOntologyCVSearch pluginOntologyCVSearch : ontologyCVSearchPlugins) {
            if (pluginOntologyCVSearch.hasPreferredResourceForCurrentField(recommendedOntologies)) return true;
        }
        return false;
    }

    public static boolean isOntologySourceAbbreviationDefinedInPlugins(String ontologySourceAbbreviation) {
        for (PluginOntologyCVSearch pluginOntologyCVSearch : ontologyCVSearchPlugins) {
            if (pluginOntologyCVSearch.getAvailableResourceAbbreviations().contains(ontologySourceAbbreviation))
                return true;
        }
        return false;
    }

    public static int howManyOfTheseResourcesAreSearchedOnByPlugins(Collection<RecommendedOntology> recommendedOntologies) {
        Set<RecommendedOntology> searchedOnResources = new HashSet<RecommendedOntology>();
        for (RecommendedOntology recommendedOntology : recommendedOntologies) {
            for (PluginOntologyCVSearch pluginOntologyCVSearch : ontologyCVSearchPlugins) {
                if (pluginOntologyCVSearch.getAvailableResourceAbbreviations().contains(recommendedOntology.getOntology().getOntologyAbbreviation()))
                    searchedOnResources.add(recommendedOntology);
            }
        }
        return searchedOnResources.size();
    }


}
