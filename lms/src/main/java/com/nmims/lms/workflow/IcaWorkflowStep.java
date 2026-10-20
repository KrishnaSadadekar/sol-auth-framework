package com.nmims.lms.workflow;

public enum IcaWorkflowStep {
    CREATE("Create ICA", 1),
    ADD_COMPONENT("Add Components", 2),
    EVALUATE("Evaluate ICA", 3),
    PUBLISH("Publish ICA", 4),
    RAISE_QUERY("Raise Query", 5),
    CONFIRM_SAP("Confirm to SAP", 6);

    private final String label;
    private final int order;

    IcaWorkflowStep(String label, int order) {
        this.label = label;
        this.order = order;
    }

    public String getLabel() {
        return label;
    }

    public int getOrder() {
        return order;
    }
}
