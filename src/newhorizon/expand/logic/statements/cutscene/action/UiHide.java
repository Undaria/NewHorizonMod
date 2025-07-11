package newhorizon.expand.logic.statements.cutscene.action;

import arc.scene.ui.layout.Table;
import mindustry.logic.*;
import newhorizon.content.NHContent;

public class UiHide extends LStatement {
    public String cutscene = "css";

    public UiHide(String[] token) {
        cutscene = token[1];
    }

    public UiHide() {
    }

    @Override
    public void build(Table table) {
        table.add(" Cutscene Name: ");
        fields(table, cutscene, str -> cutscene = str);
    }

    @Override
    public LExecutor.LInstruction build(LAssembler builder) {
        return new UIHideI(builder.var(cutscene));
    }

    @Override
    public boolean privileged() {
        return true;
    }

    @Override
    public LCategory category() {
        return NHContent.nhaction;
    }

    @Override
    public void write(StringBuilder builder) {
        builder.append("uihide");
        builder.append(" ");
        builder.append(cutscene);
    }

    @SuppressWarnings("InnerClassMayBeStatic")
    public class UIHideI implements LExecutor.LInstruction {
        public LVar cutscene;

        public UIHideI(LVar cutscene) {
            this.cutscene = cutscene;
        }

        @Override
        public void run(LExecutor exec) {
            String css = (String) cutscene.obj();
            cutscene.setobj(css + "ui_hide" + "\n");
        }
    }
}
