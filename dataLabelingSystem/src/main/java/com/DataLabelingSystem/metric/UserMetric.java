package com.DataLabelingSystem.metric;

import com.DataLabelingSystem.assignment.LabelAssignment;
import com.DataLabelingSystem.model.Dataset;
import com.DataLabelingSystem.model.Instance;
import com.DataLabelingSystem.model.Label;
import com.DataLabelingSystem.model.User;

import java.util.ArrayList;
import java.util.HashMap;

public class UserMetric {
    private User user ;
    private ArrayList<LabelAssignment> labelAssignments = new ArrayList<>();

    UserMetric(User user){
        this.user = user;
    }

    public int getNumberOfDatasets(){
        int getNumberOfDatasets = user.getAssignedDatasets().size();
        return getNumberOfDatasets;
    }

    public HashMap<Dataset,Integer> getDatasetWithCompletenessPercentage(){
        HashMap<Dataset,Integer> completenessPercentage = new HashMap<>();

        for (int i = 0; i <  user.getAssignedDatasets().size() ; i++) {
            Dataset dataset = user.getAssignedDatasets().get(i);
            completenessPercentage.put(dataset, dataset.getDatasetMetric().getCompletenessPercentage());
        }
        return completenessPercentage;
    }
    
    public int getInstancesLabeledCount(){
        int InstanceCounter = 0;

        for (int i = 0; i <  user.getAssignedDatasets().size() ; i++) {
            Dataset dataset = user.getAssignedDatasets().get(i);

            for (int j = 0; j < dataset.getLabelAssignments().size(); j++) {
                LabelAssignment labelAssignment = dataset.getLabelAssignments().get(j);
                if(labelAssignment.getUser().getId()==getUser().getId()){
                    InstanceCounter++;
                }
            }
        }
        return InstanceCounter;
    }

    public int getUniqueInstancesLabeledCount(){
        int InstanceCounter = 0;
        ArrayList<Integer> tempInstances = new ArrayList<>();

        for (int i = 0; i <  user.getAssignedDatasets().size() ; i++) {
            Dataset dataset = user.getAssignedDatasets().get(i);

            for (int j = 0; j < dataset.getLabelAssignments().size(); j++) {
                LabelAssignment labelAssignment = dataset.getLabelAssignments().get(j);
                if(labelAssignment.getUser().getId()==getUser().getId()){
                    if(!tempInstances.contains(labelAssignment.getInstanceId())){
                        tempInstances.add(labelAssignment.getInstanceId());
                    }
                }
            }
        }
        InstanceCounter = tempInstances.size();
        return InstanceCounter;
    }

    public double getConsistencyPercentage(){
        int datasetNumber = user.getAssignedDatasets().size();
        ArrayList<Label> tempLabels = new ArrayList<Label>();


        for (int i = 0; i <  user.getAssignedDatasets().size() ; i++) {
            Dataset dataset = user.getAssignedDatasets().get(i);
            for (int j = 0; j < dataset.getLabelAssignments().size(); j++) {
                LabelAssignment labelAssignment = dataset.getLabelAssignments().get(j);
                if(labelAssignment.getUser().getId()==getUser().getId()){
                    j=0;
                }
                }

            }
        return 0;
        }
    public double getAverageLabelTime(){


        return 0;
    }
    public double getStandartDeviation(){
        return 0;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ArrayList<LabelAssignment> getLabelAssignments() {
        return labelAssignments;
    }

    public void setLabelAssignments(ArrayList<LabelAssignment> labelAssignments) {
        this.labelAssignments = labelAssignments;
    }

    }


