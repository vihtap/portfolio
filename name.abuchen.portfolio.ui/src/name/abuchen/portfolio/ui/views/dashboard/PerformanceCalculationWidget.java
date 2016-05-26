package name.abuchen.portfolio.ui.views.dashboard;

import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import name.abuchen.portfolio.model.Dashboard.Widget;
import name.abuchen.portfolio.money.Values;
import name.abuchen.portfolio.snapshot.ClientPerformanceSnapshot;
import name.abuchen.portfolio.ui.util.ContextMenu;

public class PerformanceCalculationWidget extends ReportingPeriodWidget
{
    private Composite container;
    private Label title;
    private Label[] signs;
    private Label[] labels;
    private Label[] values;

    public PerformanceCalculationWidget(Widget widget, DashboardData dashboardData)
    {
        super(widget, dashboardData);
    }

    @Override
    public Composite createControl(Composite parent, DashboardResources resources)
    {
        container = new Composite(parent, SWT.NONE);
        GridLayoutFactory.fillDefaults().numColumns(3).margins(5, 5).applyTo(container);
        container.setBackground(parent.getBackground());

        title = new Label(container, SWT.NONE);
        title.setText(getWidget().getLabel());
        GridDataFactory.fillDefaults().span(3, 1).grab(true, false).applyTo(title);

        labels = new Label[ClientPerformanceSnapshot.CategoryType.values().length];
        signs = new Label[labels.length];
        values = new Label[labels.length];

        for (int ii = 0; ii < labels.length; ii++)
        {
            signs[ii] = new Label(container, SWT.NONE);
            labels[ii] = new Label(container, SWT.NONE);
            values[ii] = new Label(container, SWT.RIGHT);
            GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).applyTo(values[ii]);
        }

        return container;
    }

    @Override
    public void attachContextMenu(IMenuListener listener)
    {
        new ContextMenu(title, listener).hook();
    }

    @Override
    public void update()
    {
        title.setText(getWidget().getLabel());

        ClientPerformanceSnapshot snapshot = getDashboardData().calculate(ClientPerformanceSnapshot.class,
                        getReportingPeriod());

        int ii = 0;
        for (ClientPerformanceSnapshot.Category category : snapshot.getCategories())
        {
            signs[ii].setText(category.getSign());
            labels[ii].setText(category.getLabel());
            values[ii].setText(Values.Money.format(category.getValuation(), getClient().getBaseCurrency()));

            if (++ii >= labels.length)
                break;
        }

        container.layout();
    }
}
