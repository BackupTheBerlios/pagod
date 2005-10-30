/*
 * $Id: DPCHandler.java,v 1.1 2005/10/30 10:44:59 yak Exp $
 *
 * PAGOD- Personal assistant for group of development
 * Copyright (C) 2004-2005 IUP ISI - Universite Paul Sabatier
 *
 * This file is part of PAGOD.
 *
 * PAGOD is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * PAGOD is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with PAGOD; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */

package pagod.common.control;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javax.swing.text.html.StyleSheet;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;

import org.htmlparser.Parser;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.util.ParserException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import pagod.common.model.Activity;
import pagod.common.model.Guidance;
import pagod.common.model.Process;
import pagod.common.model.ProcessComponent;
import pagod.common.model.Product;
import pagod.common.model.Role;
import pagod.common.model.Step;
import pagod.common.model.Tool;
import pagod.common.model.WorkDefinition;

/**
 * Classe de manipulation de fichiers DPC et Pagod
 * 
 * @author m1isi24
 */
public class DPCHandler
{
    private static final String NS_XMI_URI = "http://www.omg.org/XMI";

    private static final String NS_2XMI_URI = "http://2xmi.free.fr";

    private static final String HTML_LOOKUP_FILE = "Site/main.html";

    /** Processus lu par le parser */
    private Process process;

    /** URL de base */
    private URL baseURL;

    /** Feuille de style du DPC, null si aucun */
    private StyleSheet styleSheet;

    /* Maps d'association id <> éléments de processus */
    private final HashMap<String, Role> rolesMap = new HashMap<String, Role>();

    private final HashMap<String, Activity> activitiesMap = new HashMap<String, Activity>();

    private final HashMap<String, WorkDefinition> workDefinitionsMap = new HashMap<String, WorkDefinition>();

    private final HashMap<String, ProcessComponent> processComponentsMap = new HashMap<String, ProcessComponent>();

    private final HashMap<String, Product> productsMap = new HashMap<String, Product>();

    private final HashMap<String, Guidance> guidancesMap = new HashMap<String, Guidance>();

    private final HashMap<String, String> guidanceTypesMap = new HashMap<String, String>();

    private final HashMap<String, Tool> toolsMap = new HashMap<String, Tool>();

    private final HashMap<String, Step> stepsMap = new HashMap<String, Step>();

    private final HashMap<String, URL> presentationContentsMap = new HashMap<String, URL>();

    private final HashMap<String, URL> presentationIconsMap = new HashMap<String, URL>();

    private final HashMap<String, List<Guidance>> presentationGuidancesMap = new HashMap<String, List<Guidance>>();

    /**
     * Exception levée si il y a un problème de génération de XML
     */
    public class XMLGeneratorException extends Exception
    {
        /** Constructeur vide */
        public XMLGeneratorException()
        {
            super();
        }

        /**
         * Constructeur en remontant l'exception d'origine
         * 
         * @param cause
         *            Exception d'origine
         */
        public XMLGeneratorException(Throwable cause)
        {
            super(cause);
        }
    }

    /**
     * Exception levée si il y a un problème de lecture du fichier
     */
    public class InvalidFileException extends Exception
    {

        /** Constructeur par défaut */
        public InvalidFileException()
        {
            super();
        }

        /**
         * Constructeur en remontant l'exception d'origine
         * 
         * @param cause
         *            Exception d'origine
         */
        public InvalidFileException(Throwable cause)
        {
            super(cause);
        }
    }

    /**
     * Exception levée si le fichier de configuration est incorrect
     * 
     * @author m1isi24
     */
    public class InvalidConfigurationFile extends Exception
    {
        /** Constructeur vide */
        public InvalidConfigurationFile()
        {
            super();
        }

        /**
         * Constructeur en remontant l'exception d'origine
         * 
         * @param cause
         *            Exception d'origine
         */
        public InvalidConfigurationFile(Throwable cause)
        {
            super(cause);
        }
    }

    /**
     * Construit un DPCHandler de chargement de DPC/PAGOD
     * 
     * @param baseURL
     *            URL de base du DPC
     */
    public DPCHandler(URL baseURL)
    {
        this.baseURL = baseURL;
        try
        {
            // désactive le cache sur les connexions
            // pour les problèmes de cache d'acces au JAR
            URLConnection c = baseURL.openConnection();
            c.setUseCaches(false);
            c.setDefaultUseCaches(false);
        }
        catch (IOException ioe)
        {
        }
    }

    /**
     * Construit un DPCHandler d'enregistrement de configuration
     * 
     * @param process
     *            Processus configuré
     */
    public DPCHandler(Process process)
    {
        this.process = process;
    }

    /**
     * Retourne le processus lu par le parser
     * 
     * @return Processus lu par le parser
     */
    public Process getProcess()
    {
        return this.process;
    }

    /**
     * Retourne la feuille de style extraite du DPC
     * 
     * @return Feuile de style utilisée
     */
    public StyleSheet getStyleSheet()
    {
        return this.styleSheet;
    }

    /**
     * Lit le fichier DPC et génère le processus à partir de l'url de base
     * 
     * @return TRUE si le DPC est configuré, FALSE sinon
     * @throws InvalidFileException
     *             Le fichier de processus ne peut être lu correctement
     * @throws InvalidConfigurationFile
     *             Le fichier de configuration n'est pas correct
     */
    public boolean loadDPC() throws InvalidFileException,
                            InvalidConfigurationFile
    {
        List<Element> elements;

        final URL processURL;
        final URL schemaXMIURL;
        final URL schema2XMIURL;
        try
        {
            processURL = new URL(this.baseURL, "processus.xmi");
            schemaXMIURL = new URL(this.baseURL, "xmi20.xsd");
            schema2XMIURL = new URL(this.baseURL, "2xmi.xsd");
        }
        catch (MalformedURLException mue)
        {
            throw new InvalidFileException(mue);
        }

        // création du parser DOM avec validation du schema
        final DocumentBuilderFactory documentBuilderfactory = DocumentBuilderFactory
                .newInstance();
        documentBuilderfactory.setNamespaceAware(true);

        InputStream inSchemaXMI = null;
        InputStream inSchema2XMI = null;
        try
        {
            final SchemaFactory schemaFactory = SchemaFactory
                    .newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

            inSchemaXMI = schemaXMIURL.openStream();
            inSchema2XMI = schema2XMIURL.openStream();

            Source[] sources = new Source[2];
            sources[0] = new StreamSource(inSchemaXMI);
            sources[1] = new StreamSource(inSchema2XMI);

            documentBuilderfactory.setSchema(schemaFactory.newSchema(sources));
        }
        catch (SAXException se)
        {
            throw new InvalidFileException(se);
        }
        catch (IOException ioe)
        {
            throw new InvalidFileException(ioe);
        }
        finally
        {

            try
            {
                if (inSchemaXMI != null)
                    inSchemaXMI.close();
            }
            catch (IOException ioe)
            {
            }
            try
            {
                if (inSchema2XMI != null)
                    inSchema2XMI.close();
            }
            catch (IOException ioe)
            {
            }
        }

        final DocumentBuilder documentBuilder;
        try
        {
            documentBuilder = documentBuilderfactory.newDocumentBuilder();
        }
        catch (ParserConfigurationException pce)
        {
            throw new InvalidFileException(pce);
        }

        // parse le flux
        final Document document;
        try
        {
            final InputStream processStream = processURL.openStream();
            document = documentBuilder.parse(processStream);
            processStream.close();
        }
        catch (IOException ioe)
        {
            throw new InvalidFileException(ioe);
        }
        catch (SAXException se)
        {
            throw new InvalidFileException(se);
        }

        // récupération de la racine du document
        final Element root = document.getDocumentElement();

        // création du processus
        final Element processElement = getElementNS(root, "processus");
        if (processElement == null)
        {
            throw new InvalidFileException();
        }
        this.process = new Process(this.getIdNS(processElement), processElement
                .getAttribute("nom"), null, null);

        // hasmap d'association temporaire guide -> élément présentation
        final HashMap<String, String> tmpGuidanceMap = new HashMap<String, String>();

        // lecture des éléments de présentation
        elements = this.getElementsNS(root, "elementPresentation");
        for (final Element currentElement : elements)
        {
            // ajout des infos de présentation aux maps
            final String sId = this.getIdNS(currentElement);
            if (sId.length() == 0)
                throw new InvalidFileException();

            final String sFilePath = currentElement
                    .getAttribute("cheminContenu");
            if (sFilePath.length() > 0)
            {
                try
                {
                    this.presentationContentsMap.put(sId, new URL(this.baseURL,
                            sFilePath));
                }
                catch (MalformedURLException mue)
                {
                }
            }

            final String sIconPath = currentElement.getAttribute("cheminIcone");
            if (sIconPath.length() > 0)
            {
                try
                {
                    this.presentationIconsMap.put(sId, new URL(this.baseURL,
                            sIconPath));
                }
                catch (MalformedURLException mue)
                {
                }
            }

            // note l'association id guide -> id élement présentation
            final List<Element> guidancesElements = this.getElementsNS(
                    currentElement, "guideId");
            for (final Element currentGuidanceElement : guidancesElements)
            {
                final String guideId = this.getIdRefNS(currentGuidanceElement);
                tmpGuidanceMap.put(guideId, sId);
            }
        }

        // lecture des types de guide
        elements = this.getElementsNS(root, "typeGuide");
        for (final Element currentElement : elements)
        {
            // création du rôle
            final String guidanceTypeId = this.getIdNS(currentElement);
            if (guidanceTypeId.length() == 0)
                throw new InvalidFileException();
            final String guidanceTypeName = currentElement.getAttribute("nom");

            // ajout à la map
            this.guidanceTypesMap.put(guidanceTypeId, guidanceTypeName);
        }

        // lecture des guides
        elements = this.getElementsNS(root, "guide");
        for (final Element currentElement : elements)
        {
            // récupération de l'élément de présentation associé
            final String presentationElementId = this
                    .getPresentationIdNS(currentElement);

            // récupération du type du guide
            final String typeGuidanceId = this
                    .getGuidanceTypeIdNS(currentElement);

            // création du guide
            final String guidanceId = this.getIdNS(currentElement);
            if (guidanceId.length() == 0)
                throw new InvalidFileException();
            final Guidance guidance = new Guidance(guidanceId, currentElement
                    .getAttribute("nom"), this.presentationContentsMap
                    .get(presentationElementId), this.presentationIconsMap
                    .get(presentationElementId), this.guidanceTypesMap
                    .get(typeGuidanceId));

            // ajout à la map
            this.guidancesMap.put(guidanceId, guidance);

            // ajout à l'élément de présentation correspondant
            final String linkPresentationElementId = tmpGuidanceMap
                    .get(guidanceId);
            if (linkPresentationElementId == null)
                throw new InvalidFileException();

            List<Guidance> guidances = this.presentationGuidancesMap
                    .get(linkPresentationElementId);
            if (guidances == null)
            {
                guidances = new ArrayList<Guidance>();
                this.presentationGuidancesMap.put(linkPresentationElementId,
                        guidances);
            }
            guidances.add(guidance);
        }

        // création des rôles
        elements = this.getElementsNS(root, "role");
        for (final Element currentElement : elements)
        {
            // récupération de l'élément de présentation associé
            final String presentationElementId = this
                    .getPresentationIdNS(currentElement);

            // création du rôle
            final String roleId = this.getIdNS(currentElement);
            if (roleId.length() == 0)
                throw new InvalidFileException();
            final Role role = new Role(roleId, currentElement
                    .getAttribute("nom"), this.presentationContentsMap
                    .get(presentationElementId), this.presentationIconsMap
                    .get(presentationElementId), null);

            // association avec les guides si nécessaire
            final List<Guidance> guidances = this.presentationGuidancesMap
                    .get(presentationElementId);
            if (guidances != null)
                role.setGuidances(guidances);

            // ajout à la map
            this.rolesMap.put(roleId, role);

            // ajout du role au processus
            this.process.addRole(role);
        }

        // création des produits
        elements = this.getElementsNS(root, "produit");
        for (final Element currentElement : elements)
        {
            // récupération de l'élément de présentation associé
            final String presentationElementId = this
                    .getPresentationIdNS(currentElement);

            // création du produit
            final String productId = this.getIdNS(currentElement);
            if (productId.length() == 0)
                throw new InvalidFileException();
            final Product product = new Product(productId, currentElement
                    .getAttribute("nom"), this.presentationContentsMap
                    .get(presentationElementId), this.presentationIconsMap
                    .get(presentationElementId), null);

            // association avec les guides si nécessaire
            final List<Guidance> guidances = this.presentationGuidancesMap
                    .get(presentationElementId);
            if (guidances != null)
                product.setGuidances(guidances);

            // ajout à la map
            this.productsMap.put(productId, product);
        }

        // création des activités
        elements = this.getElementsNS(root, "activite");
        for (final Element currentElement : elements)
        {
            // recherche du rôle associé à l'activité
            final Role role = this.rolesMap.get(this.getIdRefNS(currentElement,
                    "participationRole"));

            // recherche des produits...
            final ArrayList<Product> currentInProducts = new ArrayList<Product>();
            final ArrayList<Product> currentOutProducts = new ArrayList<Product>();
            List<Element> productsElements;

            // ... en entrée
            productsElements = this.getElementsNS(currentElement,
                    "entreeProduit");
            for (final Element currentInProductElement : productsElements)
            {
                currentInProducts.add(this.productsMap.get(this
                        .getIdRefNS(currentInProductElement)));
            }
            // ... en sortie
            productsElements = this.getElementsNS(currentElement,
                    "sortieProduit");
            for (final Element currentOutProductElement : productsElements)
            {
                currentOutProducts.add(this.productsMap.get(this
                        .getIdRefNS(currentOutProductElement)));
            }

            // récupération de l'élément de présentation associé
            final String presentationElementId = this
                    .getPresentationIdNS(currentElement);

            // création de l'activité
            final String activityId = this.getIdNS(currentElement);
            if (activityId.length() == 0)
                throw new InvalidFileException();
            final Activity activity = new Activity(activityId, currentElement
                    .getAttribute("nom"), this.presentationContentsMap
                    .get(presentationElementId), this.presentationIconsMap
                    .get(presentationElementId), null, null, currentInProducts,
                    currentOutProducts, role);

            // association avec les guides si nécessaire
            final List<Guidance> guidances = this.presentationGuidancesMap
                    .get(presentationElementId);
            if (guidances != null)
                activity.setGuidances(guidances);

            // ajout à la map
            this.activitiesMap.put(activityId, activity);
        }

        // création des définitions de travail
        elements = this.getElementsNS(root, "definitionTravail");
        for (Element currentElement : elements)
        {
            // création de la liste d'activités associées
            final ArrayList<Activity> currentActivities = new ArrayList<Activity>();
            final List<Element> activitiesElements = this.getElementsNS(
                    currentElement, "activiteId");
            for (Element currentActivityElement : activitiesElements)
            {
                currentActivities.add(this.activitiesMap.get(this
                        .getIdRefNS(currentActivityElement)));
            }

            // récupération de l'élément de présentation associé
            final String presentationElementId = this
                    .getPresentationIdNS(currentElement);

            // création de la définition de travail
            final String workDefinitionId = this.getIdNS(currentElement);
            if (workDefinitionId.length() == 0)
                throw new InvalidFileException();
            final WorkDefinition workDefinition = new WorkDefinition(
                    workDefinitionId, currentElement.getAttribute("nom"),
                    this.presentationContentsMap.get(presentationElementId),
                    this.presentationIconsMap.get(presentationElementId),
                    currentActivities);

            // association avec les guides si nécessaire
            final List<Guidance> guidances = this.presentationGuidancesMap
                    .get(presentationElementId);
            if (guidances != null)
                workDefinition.setGuidances(guidances);

            // ajout de la définition de travail à la map
            this.workDefinitionsMap.put(workDefinitionId, workDefinition);
        }

        // création des composants
        elements = this.getElementsNS(root, "composant");
        for (Element currentElement : elements)
        {
            // création de la liste des définitions de travail associés
            final ArrayList<WorkDefinition> currentWorkDefinitions = new ArrayList<WorkDefinition>();
            final List<Element> workDefinitionsElements = this.getElementsNS(
                    currentElement, "definitionTravailId");
            for (Element currentWorkDefinitionElement : workDefinitionsElements)
            {
                currentWorkDefinitions.add(this.workDefinitionsMap.get(this
                        .getIdRefNS(currentWorkDefinitionElement)));
            }

            // récupération de l'élément de présentation associé
            final String presentationElementId = this
                    .getPresentationIdNS(currentElement);

            // création du composant
            final String componentId = this.getIdNS(currentElement);
            if (componentId.length() == 0)
                throw new InvalidFileException();
            final ProcessComponent processComponent = new ProcessComponent(
                    componentId, currentElement.getAttribute("nom"),
                    this.presentationContentsMap.get(presentationElementId),
                    this.presentationIconsMap.get(presentationElementId),
                    currentWorkDefinitions);

            // association avec les guides si nécessaire
            final List<Guidance> guidances = this.presentationGuidancesMap
                    .get(presentationElementId);
            if (guidances != null)
                processComponent.setGuidances(guidances);

            // ajout du composant à la map
            this.processComponentsMap.put(componentId, processComponent);
        }

        // recherche du fichier CSS utilisé
        this.loadStyleSheet();
//        // application aux éléments du processus
//        ProcessElement.setStyleSheet(this.styleSheet);

        // lecture du fichier de configuration
        return this.loadConfig();
    }

    /**
     * Charge la configuration si elle existe
     * 
     * @return TRUE si il y a une configuration, FALSE sinon
     * @throws InvalidConfigurationFile
     *             Le fichier n'est pas correct (si il est absent elle n'est pas
     *             levée)
     */
    private boolean loadConfig() throws InvalidConfigurationFile
    {
        List<Element> elements;

        final URL configurationURL;
        try
        {
            configurationURL = new URL(this.baseURL, "configuration.xml");
        }
        catch (MalformedURLException mue)
        {
            throw new InvalidConfigurationFile(mue);
        }

        // création du parser DOM sans validation de schema
        final DocumentBuilderFactory documentBuilderfactory = DocumentBuilderFactory
                .newInstance();

        final DocumentBuilder documentBuilder;
        try
        {
            documentBuilder = documentBuilderfactory.newDocumentBuilder();
        }
        catch (ParserConfigurationException pce)
        {
            throw new InvalidConfigurationFile(pce);
        }

        // parse le flux
        final Document document;
        try
        {
            final InputStream configurationStream = configurationURL
                    .openStream();
            document = documentBuilder.parse(configurationStream);
            configurationStream.close();
        }
        catch (IOException ioe)
        {
            return false;
        }
        catch (SAXException se)
        {
            throw new InvalidConfigurationFile(se);
        }

        // récupération de la racine du document
        final Element root = document.getDocumentElement();

        // création des outils
        elements = this.getElements(root, "tool");
        for (Element currentElement : elements)
        {
            // création de la liste des produits associés
            final ArrayList<Product> currentProducts = new ArrayList<Product>();
            final List<Element> productsElements = this.getElements(
                    currentElement, "associatedProduct");
            for (Element currentProductElement : productsElements)
            {
                currentProducts.add(this.productsMap.get(this
                        .getIdRef(currentProductElement)));
            }

            // création de l'outil
            final String toolId = this.getId(currentElement);
            if (toolId.length() == 0)
                throw new InvalidConfigurationFile();
            final Tool tool = new Tool(toolId, currentElement
                    .getAttribute("name"), "", currentProducts);

            // ajout de l'outil à la map
            this.toolsMap.put(toolId, tool);
        }
        // ajout des outils au processus
        this.process.setTools(new ArrayList<Tool>(this.toolsMap.values()));

        // création des étapes
        elements = this.getElements(root, "activity");
        for (Element currentElement : elements)
        {
            // récupération de l'activité
            final String currentActivityId = this.getIdRef(currentElement);
            final Activity currentActivity = this.activitiesMap
                    .get(currentActivityId);
            if (currentActivity == null)
                throw new InvalidConfigurationFile();

            // création des steps
            final List<Element> stepsElements = this.getElements(
                    currentElement, "step");
            final Step[] arrSteps = new Step[stepsElements.size()];
            for (Element currentStepElement : stepsElements)
            {
                // récupération des produits
                final ArrayList<Product> outputProducts = new ArrayList<Product>();
                final List<Element> outputProductsElement = this.getElements(
                        currentStepElement, "outputProduct");
                for (Element currentOutputProductElement : outputProductsElement)
                {
                    final Product p = this.productsMap.get(this
                            .getIdRef(currentOutputProductElement));
                    if (p == null)
                        throw new InvalidConfigurationFile();
                    outputProducts.add(p);
                }

                // récupération de l'id du step
                final String stepId = this.getId(currentStepElement);
                if (stepId.length() == 0)
                    throw new InvalidConfigurationFile();

                // récupération de l'indice du step
                final int order = Integer.parseInt(currentStepElement
                        .getAttribute("order")) - 1;
                if (order < 0 || order > arrSteps.length
                        || arrSteps[order] != null)
                    throw new InvalidConfigurationFile();

                // création de l'étape
                final Step step = new Step(stepId, currentStepElement
                        .getAttribute("name"), currentStepElement
                        .getTextContent(), outputProducts);

                // ajout au tableau
                arrSteps[order] = step;

                // ajout à la map
                this.stepsMap.put(stepId, step);
            }
            final ArrayList<Step> currentSteps = new ArrayList<Step>(
                    arrSteps.length);
            for (int i = 0 ; i < arrSteps.length ; i++)
                currentSteps.add(i, arrSteps[i]);

            // ajout des étapes à l'activité
            currentActivity.setSteps(currentSteps);
        }

        return true;
    }

    /**
     * Génère le document DOM correspondant à la configuration
     * 
     * @return Document DOM correspondant à la configuration
     * @throws XMLGeneratorException
     *             Le constructeur de document DOM n'a pu être créé
     */
    public Document createConfig() throws XMLGeneratorException
    {
        final DocumentBuilderFactory fabrique = DocumentBuilderFactory
                .newInstance();
        final DocumentBuilder constructeur;
        try
        {
            constructeur = fabrique.newDocumentBuilder();
        }
        catch (ParserConfigurationException pce)
        {
            throw new XMLGeneratorException(pce);
        }
        final Document document = constructeur.newDocument();
        final Element racine = document.createElement("configuration");

        // récupération des activités via le processus
        final Collection<Role> croles = this.process.getRoles();
        for (Role role : croles)
        {
            final List<Activity> activity = role.getActivities();
            for (Activity acty : activity)
            {
                final Element activi = document.createElement("activity");
                activi.setAttribute("idref", acty.getId());

                int ordre = 1;
                final List<Step> lsteps = acty.getSteps();
                for (Step step : lsteps)
                {
                    final Element estep = document.createElement("step");
                    estep.setAttribute("id", step.getId());
                    estep.setAttribute("order", Integer.toString(ordre));
                    estep.setAttribute("name", step.getName());
                    estep.setTextContent(step.getComment());

                    final List<Product> outputProducts = step
                            .getOutputProducts();
                    for (Product oProduct : outputProducts)
                    {
                        Element eprod = document.createElement("outputProduct");
                        eprod.setAttribute("idref", oProduct.getId());

                        estep.appendChild(eprod);
                    }
                    activi.appendChild(estep);

                    ordre++;
                }
                if (!lsteps.isEmpty())
                    racine.appendChild(activi);
            }
        }

        // récupération des outils via le processus
        final Collection<Tool> tools = this.process.getTools();
        for (Tool tool : tools)
        {
            final Element etool = document.createElement("tool");
            etool.setAttribute("id", tool.getId());
            etool.setAttribute("name", tool.getName());

            List<Product> prodtool = tool.getProducts();
            for (Product producttool : prodtool)
            {
                final Element ptool = document
                        .createElement("associatedProduct");
                ptool.setAttribute("idref", producttool.getId());
                etool.appendChild(ptool);
            }

            racine.appendChild(etool);
        }

        document.appendChild(racine);

        return document;
    }

    /**
     * Cherche le fichier CSS utilisé par le site généré
     */
    private void loadStyleSheet()
    {
        try
        {
            // création de l'url
            final URL htmlFileURL = new URL(this.baseURL,
                    DPCHandler.HTML_LOOKUP_FILE);

            // création du parser HTML
            final Parser parser = new Parser(htmlFileURL.openConnection());

            // recherche du tag
            final org.htmlparser.util.NodeList nodes = parser
                    .extractAllNodesThatMatch(new AndFilter(new AndFilter(
                            new TagNameFilter("link"), new HasAttributeFilter(
                                    "type", "text/css")), new NodeClassFilter(
                            TagNode.class)));
            for (int i = 0 ; i < nodes.size() ; i++)
            {
                final TagNode node = (TagNode) nodes.elementAt(i);

                final String cssFile = node.getAttribute("href");
                // si le fichier css est spécifié
                if (cssFile != null)
                {
                    // importation du fichier
                    final URL cssFileURL = new URL(this.baseURL, "Site/"
                            + cssFile);
                    this.styleSheet = new StyleSheet();
                    this.styleSheet.importStyleSheet(cssFileURL);

                    return;
                }
            }
        }
        catch (MalformedURLException mue)
        {
        }
        catch (IOException ioe)
        {
        }
        catch (ParserException pe)
        {
        }

        this.styleSheet = null;
    }

    /*
     * Méthodes utilitaires de lecture du XMI
     */
    private Element getElementNS(Element base, String name)
    {
        final NodeList nodes = base.getElementsByTagNameNS(NS_2XMI_URI, name);
        if (nodes.getLength() == 0)
            return null;

        final Node node = nodes.item(0);
        if (node.getNodeType() != Element.ELEMENT_NODE)
            return null;

        return (Element) node;
    }

    private Element getElement(Element base, String name)
    {
        final NodeList nodes = base.getElementsByTagName(name);
        if (nodes.getLength() == 0)
            return null;

        final Node node = nodes.item(0);
        if (node.getNodeType() != Element.ELEMENT_NODE)
            return null;

        return (Element) node;
    }

    private List<Element> getElementsNS(Element baseElement, String elementsName)
    {
        final ArrayList<Element> elements = new ArrayList<Element>();

        final NodeList nodes = baseElement.getElementsByTagNameNS(NS_2XMI_URI,
                elementsName);
        for (int i = 0 ; i < nodes.getLength() ; i++)
        {
            final Node node = nodes.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE)
            {
                elements.add((Element) node);
            }
        }

        return elements;
    }

    private List<Element> getElements(Element baseElement, String elementsName)
    {
        final ArrayList<Element> elements = new ArrayList<Element>();

        final NodeList nodes = baseElement.getElementsByTagName(elementsName);
        for (int i = 0 ; i < nodes.getLength() ; i++)
        {
            final Node node = nodes.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE)
            {
                elements.add((Element) node);
            }
        }

        return elements;
    }

    private String getIdNS(Element element)
    {
        return element.getAttributeNS(NS_XMI_URI, "id");
    }

    private String getId(Element element)
    {
        return element.getAttribute("id");
    }

    private String getPresentationIdNS(Element element)
    {
        return this.getIdRefNS(element, "elementPresentationId");
    }

    private String getGuidanceTypeIdNS(Element element)
    {
        return this.getIdRefNS(element, "typeGuideId");
    }

    private String getIdRefNS(Element element)
    {
        return element.getAttributeNS(NS_XMI_URI, "idref");
    }

    private String getIdRef(Element element)
    {
        return element.getAttribute("idref");
    }

    private String getIdRefNS(Element element, String refNodeName)
    {
        final NodeList nodes = element.getElementsByTagNameNS(NS_2XMI_URI,
                refNodeName);
        if (nodes.getLength() > 0)
        {
            final Node node = nodes.item(0);
            if (node.getNodeType() == Node.ELEMENT_NODE)
                return ((Element) node).getAttributeNS(NS_XMI_URI, "idref");
        }
        return new String();
    }
}
