package com.azamat_komaev.crudapp.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class RepositoryService<T> {
    private final String FILE_PATH;
    private final Gson GSON = new Gson();

    public RepositoryService(String filePath) {
        this.FILE_PATH = filePath;
    }

    public List<T> getItemsFromFile(Class<T> modelType) {
        String fileContent;
        List<T> elementsList;
        List<T> defaultList = new ArrayList<T>(Collections.emptyList());

        try {
            fileContent = new String(Files.readAllBytes(Paths.get(this.FILE_PATH)));
            Type type = TypeToken.getParameterized(List.class, modelType).getType();
            elementsList = GSON.fromJson(fileContent, type);
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }

        return elementsList == null ? defaultList : elementsList;
    }

    public void addItemToFile(List<T> elements) {
        String json = GSON.toJson(elements);
        try {
            Files.write(Paths.get(this.FILE_PATH), json.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
