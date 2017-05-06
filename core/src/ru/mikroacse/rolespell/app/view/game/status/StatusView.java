package ru.mikroacse.rolespell.app.view.game.status;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;
import ru.mikroacse.engine.actors.RealActor;
import ru.mikroacse.engine.util.GroupUtil;
import ru.mikroacse.rolespell.app.model.game.entities.components.status.StatusComponent;
import ru.mikroacse.rolespell.app.model.game.entities.components.status.parameters.core.NumericParameter;
import ru.mikroacse.rolespell.app.model.game.entities.components.status.parameters.core.Parameter;

/**
 * Created by MikroAcse on 01-May-17.
 */
public class StatusView extends Group implements RealActor {
    private static final int PARAMETER_WIDTH = 100;
    private static final int PARAMETER_HEIGHT = 16;
    private static final int PARAMETER_OFFSET = 5;

    private StatusComponent status;
    private Array<ParameterView> parameterViews;

    private StatusComponent.Listener statusListener;

    public StatusView() {
        super();

        parameterViews = new Array<>(0);

        statusListener = parameter -> update();
    }

    public void update() {
        for (int i = 0; i < parameterViews.size; i++) {
            ParameterView parameterView = parameterViews.get(i);

            if (parameterView == null) {
                continue;
            }

            NumericParameter parameter = parameterView.getParameter();

            parameterView.setWidth(PARAMETER_WIDTH * (float) parameter.getPercentage());
            parameterView.setHeight(PARAMETER_HEIGHT);

            parameterView.setY(i * (PARAMETER_HEIGHT + PARAMETER_OFFSET));
        }
    }

    private void attachStatus(StatusComponent status) {
        status.addListener(statusListener);

        for (Parameter parameter : status.getParameters()) {
            if (!ParameterView.canBeRendered(parameter)) {
                continue;
            }

            ParameterView parameterView = new ParameterView((NumericParameter) parameter);
            parameterViews.add(parameterView);

            addActor(parameterView);
        }

        update();
    }

    private void detachStatus(StatusComponent status) {
        status.removeListener(statusListener);

        clearChildren();

        parameterViews.clear();
    }

    public StatusComponent getStatus() {
        return status;
    }

    public void setStatus(StatusComponent status) {
        if (this.status != null) {
            detachStatus(status);
        }

        this.status = status;

        if (status != null) {
            attachStatus(status);
        }
    }

    @Override
    public float getRealWidth() {
        return GroupUtil.getWidth(this);
    }

    @Override
    public float getRealHeight() {
        return GroupUtil.getHeight(this);
    }
}
