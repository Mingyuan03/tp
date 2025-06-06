package seedu.address;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.stage.Stage;
import seedu.address.commons.core.Config;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.core.Version;
import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.commons.util.ConfigUtil;
import seedu.address.commons.util.StringUtil;
import seedu.address.logic.Logic;
import seedu.address.logic.LogicManager;
import seedu.address.model.ApplicationsManager;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyApplicationsManager;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.UserPrefs;
import seedu.address.model.util.SampleDataUtil;
import seedu.address.storage.AddressBookStorage;
import seedu.address.storage.ApplicationsManagerStorage;
import seedu.address.storage.JsonAddressBookStorage;
import seedu.address.storage.JsonApplicationsManagerStorage;
import seedu.address.storage.JsonUserPrefsStorage;
import seedu.address.storage.Storage;
import seedu.address.storage.StorageManager;
import seedu.address.storage.UserPrefsStorage;
import seedu.address.ui.Ui;
import seedu.address.ui.UiManager;

/**
 * Runs the application.
 */
public class MainApp extends Application {

    public static final Version VERSION = new Version(0, 2, 2, true);

    private static final Logger logger = LogsCenter.getLogger(MainApp.class);

    protected Ui ui;
    protected Logic logic;
    protected Storage storage;
    protected Model model;
    protected Config config;

    @Override
    public void init() throws Exception {
        logger.info("=============================[ Initializing AddressBook ]===========================");
        super.init();

        AppParameters appParameters = AppParameters.parse(getParameters());
        config = initConfig(appParameters.getConfigPath());
        initLogging(config);

        UserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(config.getUserPrefsFilePath());
        UserPrefs userPrefs = initPrefs(userPrefsStorage);
        AddressBookStorage addressBookStorage = new JsonAddressBookStorage(userPrefs.getAddressBookFilePath());
        ApplicationsManagerStorage applicationsManagerStorage = new JsonApplicationsManagerStorage(
                userPrefs.getApplicationsManagerFilePath());
        storage = new StorageManager(addressBookStorage, applicationsManagerStorage, userPrefsStorage);

        model = initModelManager(storage, userPrefs);

        logic = new LogicManager(model, storage);

        ui = new UiManager(logic);
    }

    /**
     * Returns a {@code ModelManager} with the data from {@code storage}'s address
     * book and {@code userPrefs}. <br>
     * The data from the sample address book will be used instead if
     * {@code storage}'s address book is not found, or an empty address book will be
     * used instead if errors occur when reading {@code storage}'s address book.
     */
    private Model initModelManager(Storage storage, ReadOnlyUserPrefs userPrefs) {
        logger.info("Using data file : " + storage.getAddressBookFilePath());

        Optional<ReadOnlyAddressBook> addressBookOptional;
        ReadOnlyAddressBook initialAddressBookData;

        Optional<ReadOnlyApplicationsManager> applicationsManagerOptional;
        ReadOnlyApplicationsManager initialApplicationsManagerData;

        try {
            addressBookOptional = storage.readAddressBook();
            if (addressBookOptional.isEmpty()) {
                logger.info("Creating a new data file " + storage.getAddressBookFilePath()
                        + " populated with a sample AddressBook.");
                initialAddressBookData = SampleDataUtil.getSampleAddressBook();
                initialApplicationsManagerData = SampleDataUtil.getSampleApplicationsManager();
            } else {
                initialAddressBookData = addressBookOptional.get();
                // load applications manager
                try {
                    applicationsManagerOptional = storage.readApplicationsManager();
                    if (applicationsManagerOptional.isEmpty()) {
                        logger.info("Creating a new data file " + storage.getApplicationsManagerFilePath()
                                + " populated with an empty ApplicationsManager.");
                        initialApplicationsManagerData = new ApplicationsManager();
                    } else {
                        initialApplicationsManagerData = applicationsManagerOptional.get();
                    }
                } catch (DataLoadingException e) {
                    logger.warning("Data file at " + storage.getApplicationsManagerFilePath() + " could not be loaded."
                            + " Will be starting with an empty ApplicationsManager.");
                    initialApplicationsManagerData = new ApplicationsManager();
                }
            }
        } catch (DataLoadingException e) {
            // address book loading failed, use sample data for both application manager and
            // address book

            logger.warning("Data file at " + storage.getAddressBookFilePath() + " could not be loaded."
                    + " Will be starting with an some sample data.");
            initialAddressBookData = SampleDataUtil.getSampleAddressBook();
            initialApplicationsManagerData = SampleDataUtil.getSampleApplicationsManager();
        }

        return new ModelManager(initialAddressBookData, initialApplicationsManagerData, userPrefs);
    }

    private void initLogging(Config config) {
        LogsCenter.init(config);
    }

    /**
     * Returns a {@code Config} using the file at {@code configFilePath}. <br>
     * The default file path {@code Config#DEFAULT_CONFIG_FILE} will be used instead
     * if {@code configFilePath} is null.
     */
    protected Config initConfig(Path configFilePath) {
        Config initializedConfig;
        Path configFilePathUsed;

        configFilePathUsed = Config.DEFAULT_CONFIG_FILE;

        if (configFilePath != null) {
            logger.info("Custom Config file specified " + configFilePath);
            configFilePathUsed = configFilePath;
        }

        logger.info("Using config file : " + configFilePathUsed);

        try {
            Optional<Config> configOptional = ConfigUtil.readConfig(configFilePathUsed);
            if (configOptional.isEmpty()) {
                logger.info("Creating new config file " + configFilePathUsed);
            }
            initializedConfig = configOptional.orElse(new Config());
        } catch (DataLoadingException e) {
            logger.warning("Config file at " + configFilePathUsed + " could not be loaded."
                    + " Using default config properties.");
            initializedConfig = new Config();
        }

        // Update config file in case it was missing to begin with or there are
        // new/unused fields
        try {
            ConfigUtil.saveConfig(initializedConfig, configFilePathUsed);
        } catch (IOException e) {
            logger.warning("Failed to save config file : " + StringUtil.getDetails(e));
        }
        return initializedConfig;
    }

    /**
     * Returns a {@code UserPrefs} using the file at {@code storage}'s user prefs
     * file path, or a new {@code UserPrefs} with default configuration if errors
     * occur when reading from the file.
     */
    protected UserPrefs initPrefs(UserPrefsStorage storage) {
        Path prefsFilePath = storage.getUserPrefsFilePath();
        logger.info("Using preference file : " + prefsFilePath);

        UserPrefs initializedPrefs;
        try {
            Optional<UserPrefs> prefsOptional = storage.readUserPrefs();
            if (prefsOptional.isEmpty()) {
                logger.info("Creating new preference file " + prefsFilePath);
            }
            initializedPrefs = prefsOptional.orElse(new UserPrefs());
        } catch (DataLoadingException e) {
            logger.warning(
                    "Preference file at " + prefsFilePath + " could not be loaded." + " Using default preferences.");
            initializedPrefs = new UserPrefs();
        }

        // Update prefs file in case it was missing to begin with or there are
        // new/unused fields
        try {
            storage.saveUserPrefs(initializedPrefs);
        } catch (IOException e) {
            logger.warning("Failed to save config file : " + StringUtil.getDetails(e));
        }

        return initializedPrefs;
    }

    @Override
    public void start(Stage primaryStage) {
        logger.info("Starting AddressBook " + MainApp.VERSION);
        // Set minimum size constraints but don't force a specific size
        primaryStage.setMinWidth(1200);
        primaryStage.setMinHeight(800);
        ui.start(primaryStage);
    }

    @Override
    public void stop() {
        logger.info("============================ [ Stopping AddressBook ] =============================");
        try {
            storage.saveUserPrefs(model.getUserPrefs());
        } catch (IOException e) {
            logger.severe("Failed to save preferences " + StringUtil.getDetails(e));
        }
    }
}
