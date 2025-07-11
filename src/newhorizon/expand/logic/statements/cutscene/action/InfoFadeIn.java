package newhorizon.expand.logic.statements.cutscene.action;

import arc.scene.ui.layout.Table;
import mindustry.logic.*;
import newhorizon.content.NHContent;

public class InfoFadeIn extends LStatement {
    public String cutscene = "css";

    public InfoFadeIn(String[] token) {
        cutscene = token[1];
    }

    public InfoFadeIn() {
    }

    @Override
    public void build(Table table) {
        table.add(" Cutscene Name: ");
        fields(table, cutscene, str -> cutscene = str);
    }

    @Override
    public LExecutor.LInstruction build(LAssembler builder) {
        return new InfoFadeInI(builder.var(cutscene));
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
        builder.append("infofadein");
        builder.append(" ");
        builder.append(cutscene);
    }


    @SuppressWarnings("InnerClassMayBeStatic")
    public class InfoFadeInI implements LExecutor.LInstruction {
        public LVar cutscene;

        public InfoFadeInI(LVar cutscene) {
            this.cutscene = cutscene;
        }

        @Override
        public void run(LExecutor exec) {
            String css = (String) cutscene.obj();
            cutscene.setobj(css + "info_fade_in" + "\n");
        }
    }
}