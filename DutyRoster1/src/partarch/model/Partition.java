/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package partarch.model;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author jesus
 */
public class Partition {
    
    private SimpleBooleanProperty checked = new SimpleBooleanProperty(false);
    private SimpleStringProperty partitionName;
    private SimpleStringProperty highValue;
    private SimpleIntegerProperty highValueLength;
    private SimpleStringProperty tableSpace;
    private SimpleIntegerProperty numRows;
    private SimpleIntegerProperty blocks;
    private SimpleIntegerProperty emptyBlocks;
    private SimpleStringProperty lastAnalyzed;
    private SimpleIntegerProperty avgSpace;
    private SimpleIntegerProperty subpartitionCount;
    private SimpleStringProperty compression;

    public SimpleBooleanProperty checkedProperty() {
        return this.checked;
    }

    public java.lang.Boolean getChecked() {
        return this.checkedProperty().get();
    }

    public void setChecked(java.lang.Boolean checked) {
        this.checkedProperty().set(checked);
    }    
    
    public SimpleStringProperty partitionNameProperty() {
        return this.partitionName;
    }

    public java.lang.String getPartitionName() {
        return this.partitionNameProperty().get();
    }

    public void setPartitionName(java.lang.String partitionName) {
        this.partitionNameProperty().set(partitionName);
    }

    public SimpleStringProperty highValueProperty() {
        return this.highValue;
    }

    public java.lang.String getHighValue() {
        return this.highValueProperty().get();
    }

    public void setHighValue(java.lang.String highValue) {
        this.highValueProperty().set(highValue);
    }

    public SimpleIntegerProperty highValueLengthProperty() {
        return this.highValueLength;
    }

    public int getHighValueLength() {
        return this.highValueLengthProperty().get();
    }

    public void setHighValueLength(int highValueLength) {
        this.highValueLengthProperty().set(highValueLength);
    }

    public SimpleStringProperty tableSpaceProperty() {
        return this.tableSpace;
    }

    public java.lang.String getTableSpace() {
        return this.tableSpaceProperty().get();
    }

    public void setTableSpace(java.lang.String tableSpace) {
        this.tableSpaceProperty().set(tableSpace);
    }

    public SimpleIntegerProperty numRowsProperty() {
        return this.numRows;
    }

    public int getNumRows() {
        return this.numRowsProperty().get();
    }

    public void setNumRows(int numRows) {
        this.numRowsProperty().set(numRows);
    }

    public SimpleIntegerProperty blocksProperty() {
        return this.blocks;
    }

    public int getBlocks() {
        return this.blocksProperty().get();
    }

    public void setBlocks(int blocks) {
        this.blocksProperty().set(blocks);
    }

    public SimpleIntegerProperty emptyBlocksProperty() {
        return this.emptyBlocks;
    }

    public int getEmptyBlocks() {
        return this.emptyBlocksProperty().get();
    }

    public void setEmptyBlocks(int emptyBlocks) {
        this.emptyBlocksProperty().set(emptyBlocks);
    }

    public SimpleStringProperty lastAnalyzedProperty() {
        return this.lastAnalyzed;
    }

    public java.lang.String getLastAnalyzed() {
        return this.lastAnalyzedProperty().get();
    }

    public void setLastAnalyzed(java.lang.String lastAnalyzed) {
        this.lastAnalyzedProperty().set(lastAnalyzed);
    }

    public SimpleIntegerProperty avgSpaceProperty() {
        return this.avgSpace;
    }

    public int getAvgSpace() {
        return this.avgSpaceProperty().get();
    }

    public void setAvgSpace(int avgSpace) {
        this.avgSpaceProperty().set(avgSpace);
    }

    public SimpleIntegerProperty subpartitionCountProperty() {
        return this.subpartitionCount;
    }

    public int getSubpartitionCount() {
        return this.subpartitionCountProperty().get();
    }

    public void setSubpartitionCount(int subpartitionCount) {
        this.subpartitionCountProperty().set(subpartitionCount);
    }

    public SimpleStringProperty compressionProperty() {
        return this.compression;
    }

    public java.lang.String getCompression() {
        return this.compressionProperty().get();
    }

    public void setCompression(java.lang.String compression) {
        this.compressionProperty().set(compression);
    }

    public Partition(String partitionName, String highValue, int highValueLength, String tableSpace, int numRows, int blocks, int emptyBlocks, String lastAnalyzed, int avgSpace, int subpartitionCount, String compression) {
        this.partitionName = new SimpleStringProperty(partitionName);
        this.highValue = new SimpleStringProperty(highValue);
        this.highValueLength = new SimpleIntegerProperty(highValueLength);
        this.tableSpace = new SimpleStringProperty(tableSpace);
        this.numRows = new SimpleIntegerProperty(numRows);
        this.blocks = new SimpleIntegerProperty(blocks);
        this.emptyBlocks = new SimpleIntegerProperty(emptyBlocks);
        this.lastAnalyzed = new SimpleStringProperty(lastAnalyzed);
        this.avgSpace = new SimpleIntegerProperty(avgSpace);
        this.subpartitionCount = new SimpleIntegerProperty(subpartitionCount);
        this.compression = new SimpleStringProperty(compression);
    }
    

}
