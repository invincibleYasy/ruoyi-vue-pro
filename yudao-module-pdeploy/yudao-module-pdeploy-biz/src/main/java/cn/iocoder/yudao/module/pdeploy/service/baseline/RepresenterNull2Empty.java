package cn.iocoder.yudao.module.pdeploy.service.baseline;

import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Represent;
import org.yaml.snakeyaml.representer.Representer;

public class RepresenterNull2Empty extends Representer {

    public RepresenterNull2Empty() {
        super();
        this.nullRepresenter = new RepresentNull2Empty();
    }

    private  class RepresentNull2Empty implements Represent {
        public Node representData(Object data) {
            return representScalar(Tag.NULL, "");
        }
    }


}
