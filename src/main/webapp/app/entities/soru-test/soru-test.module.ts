import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { SoruTestComponent } from './list/soru-test.component';
import { SoruTestDetailComponent } from './detail/soru-test-detail.component';
import { SoruTestUpdateComponent } from './update/soru-test-update.component';
import { SoruTestDeleteDialogComponent } from './delete/soru-test-delete-dialog.component';
import { SoruTestRoutingModule } from './route/soru-test-routing.module';

@NgModule({
  imports: [SharedModule, SoruTestRoutingModule],
  declarations: [SoruTestComponent, SoruTestDetailComponent, SoruTestUpdateComponent, SoruTestDeleteDialogComponent],
  entryComponents: [SoruTestDeleteDialogComponent],
})
export class SoruTestModule {}
