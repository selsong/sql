//package org.opensearch.sql.opensearch.planner.physical;
//
//import org.apache.calcite.plan.RelOptRule;
//import org.apache.calcite.plan.RelOptRuleCall;
//import org.apache.calcite.rel.logical.LogicalSort;
//import org.apache.calcite.rel.logical.LogicalProject;
//import org.opensearch.sql.opensearch.storage.scan.CalciteLogicalIndexScan;
//
//public class OpenSearchReverseIndexScanRule extends RelOptRule {
//
//    public static final OpenSearchReverseIndexScanRule INSTANCE = new OpenSearchReverseIndexScanRule();
//
//    private OpenSearchReverseIndexScanRule() {
//        super(
//                operand(LogicalSort.class,
//                        operand(LogicalProject.class,
//                                operand(CalciteLogicalIndexScan.class, none()))),
//                "OpenSearchReverseIndexScanRule");
//    }
//
//    @Override
//    public void onMatch(RelOptRuleCall call) {
//        LogicalSort sort = call.rel(0);
//        LogicalProject project = call.rel(1);
//        CalciteLogicalIndexScan scan = call.rel(2);
//
//        if (isReversePattern(sort)) {
//            CalciteLogicalIndexScan newScan = scan.pushDownReverse();
//            if (newScan != null) {
//                call.transformTo(newScan);
//            }
//        }
//    }
//
//    private boolean isReversePattern(LogicalSort sort) {
//        // Check if this is a reverse pattern by looking for __reverse_row_num__ field
//        return sort.getCollation().getFieldCollations().stream()
//                .anyMatch(fc -> {
//                    int fieldIndex = fc.getFieldIndex();
//                    if (fieldIndex < sort.getInput().getRowType().getFieldCount()) {
//                        String fieldName = sort.getInput().getRowType().getFieldList().get(fieldIndex).getName();
//                        return "__reverse_row_num__".equals(fieldName) &&
//                               fc.getDirection() == org.apache.calcite.rel.RelFieldCollation.Direction.DESCENDING;
//                    }
//                    return false;
//                });
//    }
//}