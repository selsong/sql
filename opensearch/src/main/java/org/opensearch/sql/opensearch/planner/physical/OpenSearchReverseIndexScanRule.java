/*
 * Copyright OpenSearch Contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package org.opensearch.sql.opensearch.planner.physical;

import java.util.function.Predicate;
import org.apache.calcite.plan.RelOptRuleCall;
import org.apache.calcite.plan.RelRule;
import org.apache.calcite.rel.logical.LogicalSort;
import org.immutables.value.Value;
import org.opensearch.sql.opensearch.storage.scan.CalciteLogicalIndexScan;

@Value.Enclosing
public class OpenSearchReverseIndexScanRule extends RelRule<OpenSearchReverseIndexScanRule.Config> {

    protected OpenSearchReverseIndexScanRule(Config config) {
        super(config);
    }

    @Override
    public void onMatch(RelOptRuleCall call) {
        final LogicalSort sort = call.rel(0);
        final CalciteLogicalIndexScan scan = call.rel(1);

        CalciteLogicalIndexScan newScan = scan.pushDownReverse();
        if (newScan != null) {
            call.transformTo(newScan);
        }
    }

    /** Rule configuration. */
    @Value.Immutable
    public interface Config extends RelRule.Config {
        OpenSearchReverseIndexScanRule.Config DEFAULT =
                ImmutableOpenSearchReverseIndexScanRule.Config.builder()
                        .build()
                        .withOperandSupplier(
                                b0 ->
                                        b0.operand(LogicalSort.class)
                                                .predicate(OpenSearchIndexScanRule::isReverseSort)
                                                .oneInput(
                                                        b1 ->
                                                                b1.operand(CalciteLogicalIndexScan.class)
                                                                        .predicate(
                                                                                Predicate.not(OpenSearchIndexScanRule::isLimitPushed))
                                                                        .noInputs()));

        @Override
        default OpenSearchReverseIndexScanRule toRule() {
            return new OpenSearchReverseIndexScanRule(this);
        }
    }
}
