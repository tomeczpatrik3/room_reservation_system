import { Component, OnInit } from "@angular/core";
import {
  Validators,
  FormGroup,
  FormControl,
  FormBuilder
} from "@angular/forms";
import { ClassroomsDataService } from "../../classrooms.data.service";
import { Classroom } from "../../../../shared/models/Classroom";
import { InfoDialogComponent } from "../../../../shared/components/dialogs/info-dialog/info-dialog.component";
import { DialogService } from "../../../../shared/services/dialog.service";
import { BuildingApiService } from "../../../../shared/services/api/building.api.service";
import { TextUtils } from "../../../../shared/utils/text-utils";
import { TakenBuildingNameValidator } from "../../../../shared/directives/taken-building-name.directive";
import { ErrorDialogComponent } from "../../../../shared/components/dialogs/error-dialog/error-dialog.component";

@Component({
  selector: "app-add-classroom-form",
  templateUrl: "./add-classroom-form.component.html",
  styleUrls: ["./add-classroom-form.component.css"]
})
export class AddClassroomFormComponent implements OnInit {
  /*A betöltött épületek*/
  buildings: string[];

  /*Az épület*/
  building = new FormControl("", {
    validators: [
      Validators.required,
      Validators.minLength(3),
      Validators.maxLength(30)
    ],
    asyncValidators: this.buildingValidator.validate.bind(
      this.buildingValidator
    ),
    updateOn: "blur"
  });

  /*A tanterem neve*/
  roomName = new FormControl("", [
    Validators.required,
    Validators.minLength(3),
    Validators.maxLength(30)
  ]);

  /*A székek száma*/
  chairs = new FormControl("", [Validators.required]);

  /*PC*/
  pc = new FormControl(false);

  /*Projektor*/
  projector = new FormControl(false);

  /*Az űrlap*/
  classroomForm: FormGroup = this.builder.group({
    building: this.building,
    roomName: this.roomName,
    chairs: this.chairs,
    pc: this.pc,
    projector: this.projector
  });

  constructor(
    private builder: FormBuilder,
    private classroomService: ClassroomsDataService,
    private buildingService: BuildingApiService,
    private dialogService: DialogService,
    private buildingValidator: TakenBuildingNameValidator
  ) {}

  ngOnInit() {
    this.loadBuildings();
  }

  /**
   * Az épületek betöltéséért felelős függvény
   */
  loadBuildings() {
    this.buildingService.getNames().subscribe(res => (this.buildings = res));
  }

  /**
   * Az űrlap tanteremmé konvertálását megvalósító függvény
   */
  formToClassroom(): Classroom {
    return new Classroom(
      this.classroomForm.value.roomName,
      this.classroomForm.value.pc,
      this.classroomForm.value.projector,
      this.classroomForm.value.chairs,
      this.classroomForm.value.building
    );
  }

  /**
   * Az tanterem létrehozását megvalósító függvény
   */
  addClassroom() {
    this.classroomService
      .createClassroom(this.formToClassroom())
      .subscribe(
        () => {},
        error =>
          this.dialogService.openDialog(
            "Tanterem hozzáadása:",
            TextUtils.addBreaks(error.error),
            ErrorDialogComponent
          ),
        () =>
          this.dialogService.openDialog(
            "Tanterem hozzáadása:",
            "Tanterem sikeresen rögítve",
            InfoDialogComponent
          )
      );
    this.classroomForm.reset();
    this.loadBuildings();
  }
}
