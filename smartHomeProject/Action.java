package smartHomeProject;

public abstract class Action {
    public abstract boolean supports(Devices device);
    public abstract void apply(Devices device);
}
//
//public abstract class Action {
//	//protected Devices device;
//	
//	public Action() {
//		//this.device = device;
//	}
//	
//	public abstract void execute();
//}

//public abstract class Action {
//    public abstract void apply(Devices device);
//}